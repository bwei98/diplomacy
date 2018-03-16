import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public Unit[] retreatingUnits;
    //Countries know what units they have, units know where they are so that should be enough

    /**
     * Basic constructor for a Game state
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     * @param retreating_countries Whether you need a fucking retreat phase or not
     */
    public Game(Country[] countries, Territory[] territories, Unit[] retreating_countries) {
        this.countries = countries;
        this.territories = territories;
        this.retreatingUnits = retreating_countries;
    }

    /**
     * Basic constructor for a Game state created in Diplomacy
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     */
    public Game(Country[] countries, Territory[] territories) {
        this.countries = countries;
        this.territories = territories;
        this.retreatingUnits = new Unit[0];
    }

    /**
     * Default constructor for a Game state
     */
    public Game() {
        this.countries = null;
        this.territories = null;
        this.retreatingUnits = new Unit[0];
    }

    /**
     * buildphase processses a single movement phase
     * In case of incorrect orders:
     * Incorrect disbands will cause the buildphase to randomly select units to disband
     * Incorrect builds will result in a country losing that build
     * If more disbands or builds are entered than the country needs, they will not go through after
     * they are no longer necessary
     * If a country has no units, the country is declared dead here
     * @param orders
     * @return
     */
    public Game buildphase(String[][] orders) {
        for(int id = 0; id < orders.length; id++) {
            Country country = countries[id];
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                boolean isFleet = order[0].trim().equals("F");
                String location = order[1].trim();

                int diff = country.numBuildsOrDisbands();
                if (diff < 0) { //process as a build
                    for (Territory sc : country.homeSCs) {
                        if (sc.equals(location) && sc.occupied == -1) {
                            country.build(sc, isFleet);
                        }
                    }
                } else { //process as a disband
                    for (Unit unit : country.units) {
                        if (unit.location.equals(location)) {
                            country.disband(unit);
                        }
                    }
                }

                while(country.numBuildsOrDisbands() > 0) country.disband(country.units[0]);
            }

            if(country.units.length == 0) country.alive = false;
        }

        return this;
    }

    /**
     * Process retreat orders in basically the most inefficient way possible
     * Invalid orders are disbands
     * If two units try to retreat to the same place, they are both disbanded
     * @param orders The desired retreats, indexed to match with retreats
     * @return The new game state
     */
    public Game retreatphase(String[] orders) {
        //Process input into valid Territory moves
        Territory[] dests = new Territory[orders.length];

        for(int i = 0; i < retreatingUnits.length; i++) {
            Unit unit = retreatingUnits[i];
            String dest = orders[i];
            if(dest.toUpperCase().equals("DISBAND")){
                dests[i] = null;
            } else {
                boolean found = false;
                for (Territory t : unit.canMove()) {
                    if (t.equals(dest)) {
                        found = true;
                        dests[i] = t;
                        break;
                    }
                }
                if (!found) dests[i] = null;
            }
        }

        //Check for conflicts
        Territory[] places = new Territory[retreatingUnits.length];
        int[] placesCount = new int[retreatingUnits.length];
        for(int i = 0; i < retreatingUnits.length; i++) {
            Unit unit = retreatingUnits[i];
            Territory dest = dests[i];

            for(int j = 0; j < places.length; j++) {
                if(placesCount[j] == 0) {
                    places[j] = dest;
                    placesCount[j]++;
                } else {
                    if(places[j] == dest) placesCount[j]++;
                }
            }
        }

        //Set to disband if there is a conflict
        for(int i = 0; i < dests.length; i++) {
            for(int j = 0; j < places.length; j++) {
                if(placesCount[j] > 1 && dests[i] == places[j]) dests[i] = null;
            }
        }

        //Perform retreats
        for(int i = 0; i < retreatingUnits.length; i++) {
            Unit unit = retreatingUnits[i];
            Territory dest = dests[i];
            if(dest == null) unit.owner.disband(unit);
            else unit.move(dest);
        }

        retreatingUnits = new Unit[0];
        return this;
    }

    /**
     * movephase processes a single movement phase
     * @param orders String[][] for countries and their orders
     * @return Game representing the new gamestate
     */
    public Game movephase(String[][] orders) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Unit> retreats = new ArrayList<>();
        Game g = new Game();

        //reads orders into moves arraylist
        for(int id=0; id<orders.length; id++) {
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                for(String s: order)    s=s.trim();
                System.out.println("movephase, id: " + id);
                Move m=new Move(this, id, order);
                moves.add(m);
            }
        }

        for(Move m : moves)
            System.out.println(m.toString());

        //determine which are valid
        while(countPending(moves)>0)
            resolution(moves);

        //remove all unnecessary
        moves.removeIf(m -> (m.status!=Status.EXECUTABLE));

        System.out.println("NumExecutable = " + countExecutable(moves));
        //executes moves
        while(countExecutable(moves)>0) {
            g = this.execution(moves);
            moves.removeIf(m -> (m.status==Status.EXECUTED));
            System.out.println("NumExecutable = " + countExecutable(moves));
        }

        //reset
        for(Territory t : this.territories)
            Arrays.fill(t.takeStrength, 0);
        for(Country c : g.countries)
            for(Unit u : c.units)
                u.hasOrder=false;

        return g;
    }

    /**
     * Resolves at least one pending move
     * @param moveset arraylist of moves
     */
    private void resolution(ArrayList<Move> moveset){
        //@ENSURES countPending(moveset)>countPending(moveset')
        for(Move m : moveset) {
            if (m.status == Status.FAILED)
                moveset.remove(m);
            if (m.status == Status.PENDING && m.type == Type.H) {
                m.destination.takeStrength[m.country.id] += 2;
                m.status = Status.EXECUTABLE;
                return;
            }
        }
        for(Move m : moveset) {
            if (m.status == Status.PENDING && m.type == Type.M) {
                m.destination.takeStrength[m.country.id]++;
                //what if you *attack* at territory with two of your units (not support);
                //we have no way of dealing with that;
                m.status = Status.EXECUTABLE;
                return;
            }
        }
        for(Move m : moveset) {
            if (m.status == Status.PENDING && m.type == Type.SH) {
                for (Move n : moveset)
                    if (!m.equals(n) && n.destination.equals(m.destination) && n.type != Type.M
                            && n.type != Type.CM && n.type != Type.D) {
                        m.status = Status.EXECUTABLE;
                        m.destination.takeStrength[n.country.id]++;
                        return;
                    }
                m.status = Status.FAILED;
                return;
            }
        }
        for(Move m : moveset) {
            if (m.type == Type.C) {
                for (Move n : moveset)
                    if (n.type == Type.CM && n.unit.location.equals(m.source) && n.destination.equals(m.destination)) {
                        m.status = Status.EXECUTABLE;
                        return;
                    }
                m.status = Status.FAILED;
                return;
            }
        }
        for(Move m : moveset) {
            if (m.type == Type.CM) {
                //continuations would actually be kinda useful here
                ArrayList<Territory> visited = new ArrayList<>();
                Territory start = m.unit.location;
                visited.add(start);
                if (convoyMove(m, moveset, visited, start)) {
                    m.status = Status.EXECUTABLE;
                    m.type = Type.M;
                    m.destination.takeStrength[m.country.id]++;
                    return;
                } else {
                    m.status = Status.FAILED;
                    return;
                }
            }
        }
        for(Move m : moveset) {
            if (m.type == Type.SA) {
                for (Move n : moveset)
                    if (n.type == Type.M && n.unit.location.equals(m.source) && n.destination.equals(m.destination)) {
                        m.status = Status.EXECUTABLE;
                        m.destination.takeStrength[n.country.id]++;
                        return;
                    }
                m.status = Status.FAILED;
                return;
            }
        }
    }

    /**
     * checks if a convoy move is valid, recursive backtracking
     * @param m move in question
     * @param moveset other moves for the turn
     * @param visited path
     * @param loc current
     * @return is it valid?
     */
    private boolean convoyMove(Move m, ArrayList<Move> moveset, ArrayList<Territory> visited, Territory loc){
        visited.add(loc);
        if(m.destination.equals(loc)) return true;
        boolean success = false;
        for(Territory t: loc.neighborsF) {
            for (Move n : moveset) {
                if (n.type == Type.C && n.source.equals(m.unit.location) &&
                        n.destination.equals(m.destination) && !visited.contains(n.unit.location) && !success) {
                    success = convoyMove(m, moveset, visited, t);
                }
            }

        }
        if(!success) visited.remove(loc);
        return success;
    }

    /**
     * Resolves at least one executable move
     * @param moveset arraylist of moves
     * @return updated of arraylist of retreats needed
     */
    private Game execution(ArrayList<Move> moveset) {
        //@ENSURES countExecutable(moveset)>countExecutable(moveset')
        System.out.println("entering execution");
        ArrayList<Unit> retreats = new ArrayList<>();
        Unit[] newRetreats = this.retreatingUnits;
        Move m = moveset.get(0);
        Territory t = m.destination;
        //TODO if mostPow is tie then all orders fail
        int mostPow = 0;
        int mostPowCountry = t.occupied;
        for (int i = 0; i < t.takeStrength.length; i++){
            if (t.takeStrength[i] > mostPow) {
                mostPow = t.takeStrength[i];
                mostPowCountry = i;
            }
    }
        if(t.occupied!=mostPowCountry){
            Collections.addAll(retreats, this.retreatingUnits);
            if(t.occupied != -1) {
                for (Unit unit : this.countries[t.occupied].units)
                    if (unit.location.equals(t))
                        retreats.add(unit);
                newRetreats = new Unit[retreats.size()];
                newRetreats = retreats.toArray(newRetreats);
            }
            for(Move n : moveset) {
                System.out.println(n.toString());
                System.out.println(t.toString());
                System.out.println(mostPowCountry);
                if (n.destination.equals(t) && n.country.id == mostPowCountry) {
                    n.unit.location = t;
                    t.occupied = mostPowCountry;
                    break;
                }
            }
        }
        for(Move n : moveset)
            if(n.destination.equals(t))
                n.status=Status.EXECUTED;
        for(Country c : this.countries)
            for(Unit u : c.units)
                System.out.println(c.name + " " + u.toString());
        return new Game(this.countries, this.territories, newRetreats);
    }

    /**
     * Number of PENDING orders in a moveset
     * @param moveset an ArrayList of moves
     * @return int for the number of pending orders in the list
     */
    private static int countPending(ArrayList<Move> moveset) {
        int numPending=0;
        for(Move a : moveset)
            if(a.status==Status.PENDING)
                numPending++;
        return numPending;
    }

    /**
     * Number of EXECUTable orders in a moveset
     * @param moveset an ArrayList of moves
     * @return int for the number of executable orders in the list
     */
    private static int countExecutable(ArrayList<Move> moveset) {
        int numExecutable=0;
        for(Move a : moveset)
            if(a.status==Status.EXECUTABLE)
                numExecutable++;
        return numExecutable;
    }


    //## : A Lvn H   (7)
    //## : A Lvn - War  (11)
    //## : A Lvn S War
    //## : A Lvn S Ukr - War (17)
    //## : A Lvn S War (11)
    //## : F NTH C Nwy - Yor (17)

}

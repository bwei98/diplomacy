import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public Unit[] retreating_units;
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
        this.retreating_units = retreating_countries;
    }

    /**
     * Basic constructor for a Game state created in Diplomacy
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     */
    public Game(Country[] countries, Territory[] territories) {
        this.countries = countries;
        this.territories = territories;
        this.retreating_units = new Unit[0];
    }

    /**
     * Default constructor for a Game state
     */
    public Game() {
        this.countries = null;
        this.territories = null;
        this.retreating_units = new Unit[0];
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
                        if (sc.name.equals(location) && sc.occupied == -1) {
                            country.build(sc, isFleet);
                        }
                    }
                } else { //process as a disband
                    for (Unit unit : country.units) {
                        if (unit.location.name.equals(location)) {
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
                Move m=new Move(this, id, order);
                moves.add(m);
            }
        }

        //determine which are valid
        while(countPending(moves)>0)
            resolution(moves);

        //remove all unnecessary
        for(Move m : moves)
            if(m.status!=Status.EXECUTABLE)
                moves.remove(m);

        //executes moves
        while(countExecutable(moves)>0) {
            for(Move m : moves)
                if(m.status==Status.EXECUTED)
                    moves.remove(m);
            g = execution(moves, g);
        }

        //clear takeStrength
        for(Territory t : this.territories)
            Arrays.fill(t.takeStrength, 0);

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
     * @param g game with state
     * @return updated of arraylist of retreats needed
     */
    private Game execution(ArrayList<Move> moveset, Game g) {
        //@ENSURES countExecutable(moveset)>countExecutable(moveset')
        ArrayList<Unit> retreats = new ArrayList<>();
        Unit[] newRetreats = g.retreating_units;
        Move m = moveset.get(0);
        Territory t = m.destination;
        int mostPowCountry = Collections.max(Arrays.asList(t.takeStrength));
        if(t.occupied!=mostPowCountry){
            Collections.addAll(retreats, g.retreating_units);
            for(Unit unit : g.countries[t.occupied].units)
                if(unit.location.equals(t))
                    retreats.add(unit);
            newRetreats = new Unit[retreats.size()];
            newRetreats = retreats.toArray(newRetreats);
            for(Move n : moveset)
                if(n.destination.equals(t) && n.country.id==mostPowCountry) {
                    n.unit.location = t;
                    t.occupied = mostPowCountry;
                    break;
                }
        }
        for(Move n : moveset)
            if(n.destination.equals(t))
                n.status=Status.EXECUTED;
        return new Game(g.countries, g.territories, newRetreats);
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



    public Game retreat(String[] orders) {
        return new Game();
    }

    //## : A Lvn H   (7)
    //## : A Lvn - War  (11)
    //## : A Lvn S War
    //## : A Lvn S Ukr - War (17)
    //## : A Lvn S War (11)
    //## : F NTH C Nwy - Yor (17)

}

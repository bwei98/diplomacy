import org.apache.commons.lang3.tuple.MutableTriple;
import java.util.ArrayList;
import java.util.Collections;


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
     * buildphase processes a single movement phase
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
                    for (Territory sc : country.getHomeSCs()) {
                        if (sc.equals(location) && sc.getOccupied() == -1) {
                            if(!(isFleet && sc.landlocked())) country.build(sc, isFleet);
                        }
                    }
                } else { //process as a disband
                    for (Unit unit : country.getUnits()) {
                        if (unit.getLocation().equals(location)) {
                            country.disband(unit);
                        }
                    }
                }

                while(country.numBuildsOrDisbands() > 0) country.disband(country.getUnits()[0]);
            }

            if(country.getUnits().length == 0) country.setAlive(false);
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
//            Unit unit = retreatingUnits[i];
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
            if(dest == null) unit.getOwner().disband(unit);
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
        ArrayList<Move> moves;
        moves = new ArrayList<>();
//        ArrayList<Unit> retreats = new ArrayList<>();
        Game g = new Game();

        //reads orders into moves arraylist
        for(int id=0; id<orders.length; id++) {
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                for(String s: order) s = s.trim();
//                System.out.println("movephase, id: " + id);
                Move m=new Move(this, id, order);
                moves.add(m);
            }
        }

//        for(Move m : moves)
//            System.out.println(m.toString());

        //determine which are valid
        while(countPending(moves)>0) {
            resolution(moves);
        }
        //remove all unnecessary
        moves.removeIf(m -> (m.getStatus()!=Status.EXECUTABLE));

//        System.out.println("NumExecutable = " + countExecutable(moves));
        // sort so territories in a convoy are first
        Move.sortC(moves);
//        executes moves
        while(countExecutable(moves)>0) {
            g = this.execution(moves);
            moves.removeIf(m -> (m.getStatus()==Status.EXECUTED));
//            System.out.println("NumExecutable = " + countExecutable(moves));
        }

        //reset
        for(Territory t : this.territories)
            t.setAttacks(new ArrayList<>());
        for(Country c : this.countries)
            for(Unit u : c.getUnits())
                u.setHasOrder(false);

        g.territories = this.territories;
        g.countries = this.countries;
        return g;
    }

    /**
     * Resolves at least one pending move
     * @param moveset arraylist of moves
     */
    private void resolution(ArrayList<Move> moveset){
        //@ENSURES countPending(moveset)>countPending(moveset')
        moveset.removeIf(m -> (m.getStatus()==Status.FAILED));
        for(Move m : moveset) {
            if (m.getStatus() == Status.PENDING && m.getType() == Type.H) {
                m.getDestination().getAttacks().add(new MutableTriple<>(m, m.getCountry(), 2));
                m.setStatus(Status.EXECUTABLE);
                return;
            }
        }
        for(Move m : moveset) {
            if (m.getStatus() == Status.PENDING && m.getType() == Type.M) {
                m.getDestination().getAttacks().add(new MutableTriple<>(m, m.getCountry(), 1));
                m.setStatus(Status.EXECUTABLE);
                return;
            }
        }
        for(Move m : moveset) {
            //TODO Fail convoys when they are displaced
            if (m.getStatus() == Status.PENDING && m.getType() == Type.C) {
                for (Move n : moveset)
                    if (n.getType() == Type.CM && n.getUnit().getLocation().equals(m.getSource())
                            && n.getDestination().equals(m.getDestination())) {
                        m.setStatus(Status.EXECUTABLE);
                        return;
                    }
                m.setStatus(Status.FAILED);
                return;
            }
        }
        for(Move m : moveset) {
            if (m.getStatus() == Status.PENDING && m.getType() == Type.CM) {
                //continuations would actually be kinda useful here
                ArrayList<Territory> visited = new ArrayList<>();
                Territory start = m.getUnit().getLocation();
                visited.add(start);
                if (convoyMove(m, moveset, visited, start)) {
                    m.setStatus(Status.EXECUTABLE);
                    m.getDestination().getAttacks().add(new MutableTriple<>(m, m.getCountry(), 1));
                    return;
                } else {
                    m.setStatus(Status.FAILED);
                    return;
                }
            }
        }
        for(Move m : moveset) {
            if (m.getStatus() == Status.PENDING && m.getType() == Type.SH) {
                for (MutableTriple<Move, Country, Integer> t : m.getDestination().getAttacks()) {
                    Move n = t.getLeft();
                    if (n.getDestination().equals(m.getUnit().getLocation()) && (n.getType() == Type.M
                            || n.getType() == Type.CM)){
                        m.setStatus(Status.FAILED);
                        return;
                    }
                }
                for (MutableTriple<Move, Country, Integer> t : m.getDestination().getAttacks()) {
                    Move n = t.getLeft();
                    if (m.getDestination().equals(n.getDestination()) && n.getType() != Type.M
                            && n.getType() != Type.CM && n.getType() != Type.D) {
                        m.setStatus(Status.EXECUTABLE);
                        t.setRight(t.right++);
                        return;
                    }
                }
                m.setStatus(Status.FAILED);
                return;
            }
        }
        for(Move m : moveset) {
            if (m.getStatus() == Status.PENDING && m.getType() == Type.SA) {
                for (MutableTriple<Move, Country, Integer> t : m.getDestination().getAttacks()) {
                    Move n = t.getLeft();
                    if (n.getDestination().equals(m.getUnit().getLocation()) && (n.getType() == Type.M || n.getType() == Type.CM)){
                        m.setStatus(Status.FAILED);
                        return;
                    }
                }
                for (MutableTriple<Move, Country, Integer> t : m.getDestination().getAttacks()) {
                    Move n = t.getLeft();
                    if ((n.getType() == Type.M || n.getType() == Type.CM) && n.getUnit().getLocation().equals(m.getSource()) &&
                            n.getDestination().equals(m.getDestination())) {
                        m.setStatus(Status.EXECUTABLE);
                        t.setRight(t.right++);
                        return;
                    }
                }
                m.setStatus(Status.FAILED);
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
//        System.out.println("Call to convoyMove: " + m.toString() + " Moveset: " + moveset.toString() + " \n visited: " + visited.toString() + " loc: " + loc.toString());
        visited.add(loc);
        if(m.getDestination().equals(loc)) return true;
        boolean success = false;
        for(Territory t: loc.getNeighborsF()) {
            for (Move n : moveset) {
                if (n.getType() == Type.C && n.getSource().equals(m.getUnit().getLocation()) &&
                        n.getDestination().equals(m.getDestination()) && !visited.contains(t) && !success) {
                    success = convoyMove(m, moveset, visited, t);
                }
            }
        }
        //if(!success) visited.remove(loc);
        return success;
    }

    /**
     * Resolves at least one executable move
     * @param moveset arraylist of moves
     * @return updated of arraylist of retreats needed
     */
    private Game execution(ArrayList<Move> moveset) {
        //@ENSURES countExecutable(moveset)>countExecutable(moveset')
        ArrayList<Unit> retreats = new ArrayList<>();
        Unit[] newRetreats = this.retreatingUnits;
        Move m = moveset.get(0);
        Territory t = m.getDestination();

        int mostPowCountry = tripArrLstMaxTies(t.getAttacks(), t.getOccupied());

        if(t.getOccupied()!=mostPowCountry){

            // if it's part of a convoy and dies then kill everything related to it
            if(m.getType() == Type.C){
                for(Move n : moveset){
                    if(n.getDestination() == m.getDestination() && (n.getType() == Type.C || n.getType() == Type.CM)){
                        moveset.remove(n);
                    }
                }
            }

            Collections.addAll(retreats, this.retreatingUnits);
            if(t.getOccupied() != -1) {
                for (Unit unit : this.countries[t.getOccupied()].getUnits())
                    if (unit.getLocation().equals(t)) {
                        retreats.add(unit);
                    }
                newRetreats = new Unit[retreats.size()];
                newRetreats = retreats.toArray(newRetreats);

            }
            for(Move n : moveset) {
                System.out.println(n.toString());
                System.out.println(t.toString());
                System.out.println(mostPowCountry);
                if (n.getDestination().equals(t) && n.getCountry().getId() == mostPowCountry) {
                    n.getUnit().setLocation(t);
                    t.setOccupied(mostPowCountry);
                    break;
                }
            }
        }
        for(Move n : moveset)
            if(n.getDestination().equals(t))
                n.setStatus(Status.EXECUTED);
//        for(Country c : this.countries)
//            for(Unit u : c.units)
//                System.out.println(c.name + " " + u.toString());
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
            if(a.getStatus()==Status.PENDING)
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
            if(a.getStatus()==Status.EXECUTABLE)
                numExecutable++;
        return numExecutable;
    }


    /**
     * Returns int for the index of the maximum of the array or a default value if there is a tie
     * @param arr int array
     * @param def default value
     * @return index of max of arr or def if the max is a tie
     */
    private static int arrMaxTies(Integer[] arr, int def){
        int ind;
        if(arr[0]< arr[1])
            ind = 1;
        else
            ind = 0;
        int max1 = Integer.max(arr[0],arr[1]);
        int max2 = Integer.min(arr[0],arr[1]);
        for(int i=2; i<arr.length; i++){
            if(arr[i]>max1){
                max2=max1;
                max1=arr[i];
                ind = i;
            }
            else if(arr[i]>max2){
                max2=arr[i];
            }
        }
        if(max1==max2) return def;
        return ind;
    }

    /**
     * Returns int for the index of the maximum attack of the arrayList or a default value if the max is a tie
     * @param atks ArrayList<MutableTriple<Move,Country,Integer>> representing the attacks
     * @param def default value
     * @return index of max of atks or def if the max is a tie
     */
    private static int tripArrLstMaxTies(ArrayList<MutableTriple<Move,Country,Integer>> atks, int def){
        int ind = -1;
        if(atks.size() < 2) return 0;
        int max1 = -1;
        int max2 = -2;
        for(int i=0; i<atks.size(); i++){
            int irt = atks.get(i).right;
            if(irt>max1){
                max2=max1;
                max1=irt;
                ind = atks.get(i).middle.getId();
            }
            else if(irt>max2){
                max2=irt;
            }
        }
        if(max1==max2) return def;
        return ind;
    }

    //## : A Lvn H   (7)
    //## : A Lvn - War  (11)
    //## : A Lvn S War
    //## : A Lvn S Ukr - War (17)
    //## : A Lvn S War (11)
    //## : F NTH C Nwy - Yor (17)

}

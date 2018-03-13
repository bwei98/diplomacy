import java.util.ArrayList;
import java.util.Collections;

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public Unit[] retreating_units;
    //Countries know what units they have, units know where they are so that should be enough
    public static ArrayList<Territory> checked = new ArrayList<Territory>();

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
     * movephase processes a single movement phase
     * @param orders String[][] for countries and their orders
     * @return Game representing the new gamestate
     */
    public Game movephase(String[][] orders) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Unit> retreats = new ArrayList<>();
        for(int id=0; id<orders.length; id++) {
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                for(String s: order)    s=s.trim();
                Move m=new Move(this, id, order);
                moves.add(m);
            }
        }
        while(countPending(moves)>0)
            resolution(moves);

        while(countExecutable(moves)>0)
            retreats = execution(moves, retreats);


        //TODO returning
        return new Game();
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
            if (m.status == Status.PENDING) {
                if (m.type == Type.H) {
                    m.destination.takeStrength[m.country.id] += 2;
                    m.status = Status.EXECUTABLE;
                    return;
                }
                if (m.type == Type.M) {
                    m.destination.takeStrength[m.country.id]++;
                    //what if you *attack* at territory with two of your units (not support);
                    //we have no way of dealing with that;
                    m.status = Status.EXECUTABLE;
                    return;
                }
                if (m.type == Type.SH) {
                    for (Move n : moveset)
                        if (!m.equals(n) && n.destination.equals(m.destination) && n.type != Type.M
                                && n.type != Type.CM && n.type != Type.D) {
                            m.status = Status.EXECUTABLE;
                            m.destination.takeStrength[n.country.id]++;
                            return;
                        }
                    m.status= Status.FAILED;
                }
                if(m.type==Type.C) {
                    for(Move n : moveset)
                        if(n.type==Type.CM && n.unit.location.equals(m.source) && n.destination.equals(m.destination)){
                            m.status=Status.EXECUTABLE;
                            return;
                        }
                    m.status=Status.FAILED;
                }
                if(m.type==Type.CM){
                    //continuations would actually be kinda useful here
                    ArrayList<Territory> visited = new ArrayList<>();
                    Territory start = m.unit.location;
                    visited.add(start);
                    if(convoyMove(m, moveset, visited, start)) {
                        m.status=Status.EXECUTABLE;
                        m.type = Type.M;
                        m.destination.takeStrength[m.country.id]++;
                    } else
                        m.status=Status.FAILED;
                }
                if(m.type==Type.SA){
                    for(Move n : moveset)
                        if(n.type == Type.M && n.unit.location.equals(m.source) && n.destination.equals(m.destination)){
                            m.status=Status.EXECUTABLE;
                            m.destination.takeStrength[n.country.id]++;
                            return;
                        }
                    m.status=Status.FAILED;
                }


            }
        }

    }


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
     * @param retreatsQueued arraylist of retreats needed
     * @return updated of arraylist of retreats needed
     */
    private ArrayList<Unit> execution(ArrayList<Move> moveset, ArrayList<Unit> retreatsQueued) {
        //@ENSURES countExecutable(moveset)>countExecutable(moveset')
        //TODO

        return retreatsQueued;
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
        int numPending=0;
        for(Move a : moveset)
            if(a.status==Status.EXECUTABLE)
                numPending++;
        return numPending;
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

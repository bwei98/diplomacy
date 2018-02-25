import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public Country[] retreating_countries;
    //Countries know what units they have, units know where they are so that should be enough


    /**
     * Basic constructor for a Game state
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     * @param retreating_countries Whether you need a fucking retreat phase or not
     */
    public Game(Country[] countries, Territory[] territories, Country[] retreating_countries) {
        this.countries = countries;
        this.territories = territories;
        this.retreating_countries = retreating_countries;
    }

    /**
     * Basic constructor for a Game state created in Diplomacy
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     */
    public Game(Country[] countries, Territory[] territories) {
        this.countries = countries;
        this.territories = territories;
        this. = new Country[0];
    }

    /**
     * Default constructor for a Game state
     */
    public Game() {
        this.countries = null;
        this.territories = null;
        this. = new Country[0];
    }

    /**
     * movephase processes a single movement phase
     * @param orders String[][] for countries and their orders
     * @return Game representing the new gamestate
     */
    public Game movephase(String[][] orders) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Move> retreats = new ArrayList<>();
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
            retreats = executaion(moves, retreats);


        //TODO returning
        return new Game();
    }

    /**
     * Resolves at least one pending move
     * @param moveset arraylist of moves
     */
    private void resolution(ArrayList<Move> moveset){
        //@ENSURES countPending(moveset)>countPending(moveset')
        //TODO
    }

    /**
     * Resolves at least one executable move
     * @param moveset arraylist of moves
     * @param retreatsQueued arraylist of retreats needed
     * @return updated of arraylist of retreats needed
     */
    private ArrayList<Move> executaion(ArrayList<Move> moveset, ArrayList<Move> retreatsQueued) {
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

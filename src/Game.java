import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public boolean doIneedafuckingretreat;
    //Countries know what units they have, units know where they are so that should be enough^


    /**
     * Basic constructor for a Game state
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     * @param doIneedafuckingretreat Whether you need a fucking retreat phase or not
     */
    public Game(Country[] countries, Territory[] territories, boolean doIneedafuckingretreat) {
        this.countries = countries;
        this.territories = territories;
        this.doIneedafuckingretreat = doIneedafuckingretreat;
    }

    /**
     * Basic constructor for a Game state created in Diplomacy
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the TERRITORIES
     */
    public Game(Country[] countries, Territory[] territories) {
        this.countries = countries;
        this.territories = territories;
        this.doIneedafuckingretreat = false;
    }

    /**
     * Default constructor for a Game state
     */
    public Game() {
        this.countries = null;
        this.territories = null;
        this.doIneedafuckingretreat = false;
    }

    /**
     * movephase processes a single movement phase
     * @param orders String[][] for countries and their orders
     * @return Game representing the new gamestate
     */
    public Game movephase(String[][] orders) {
        ArrayList<Move> Moves = new ArrayList<Move>();
        for(int id=0; id<orders.length; id++) {
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                //order[0]=A or F, order[1]=start loc, order[2]=type 1, order[3]=dest, order[4]="-", order[5]=dest2
                for(String s: order)    s=s.trim();
                Move m=new Move(this, id, order);
                Moves.add(m);
            }
        }
        /*
        while(some moves are pending)
            resolve them
         */
        //TODO returning
        return new Game();
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

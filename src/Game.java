

public class Game {
    public Country[] countries;
    public Territory[] territories;
    public boolean doIneedafuckingretreat;
    //Countries know what units they have, units know where they are so that should be enough^


    /**
     * Basic constructor for a Game state
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the territories
     */
    public Game(Country[] countries, Territory[] territories, boolean doIneedafuckingretreat) {
        this.countries=countries;
        this.territories=territories;
        this.doIneedafuckingretreat=doIneedafuckingretreat;
    }

    /**
     * Default constructor for a Game state
     */
    public Game() {
        this.countries=null;
        this.territories=null;
        this.doIneedafuckingretreat=false;
    }

    public Game progress(String[] orders) {
        for(string o : orders) {
            String[] orderset = o.split(" : ");
            //order[0]=country.id   order[1]=order
            String[] type = orderset[1].split(" ");
            String[] startloc = type[1].split(" ");

        }
        return new Game();
    }



    //## : A Lvn H   (7)
    //## : A Lvn - War  (11)
    //## : A Lvn S Ukr - War (17)
    //## : A Lvn S War (11)
    //## : F NTH C Nwy - Yor (17)




}

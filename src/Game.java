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

    public Game movephase(String[] orders) {
        for(string o : orders) {
            String[] orderset = o.split(" : ");
            //orderset[0]=country.id
            String[] type = orderset[1].split(" ");
            //type[0]=A or F
            String[] startloc = type[1].split(" ");
            //startloc[0]= starting location
            int id= String.parseint(orderset[0]);
            //check if ownership is valid
            boolean unitcheck=true;
            for(Unit u : this.countries[id].units)
                if((u.isFleet && type[0].equals("F") || !u.isFleet && type[0].equals("A"))
                        && (u.location.name.equals(startloc[0]))) {
                    unitcheck = false;
                    break;
                }
            if(unitcheck)
                System.out.println("Order: "+o+" failed becuase you do not own a "+type[0]+"in "+startloc[0]);
            String[] movetype = startloc[1].split(" ");
            //movetype[0]= H, -, S, C


        }
        return new Game();
    }



    //## : A Lvn H   (7)
    //## : A Lvn - War  (11)
    //## : A Lvn S Ukr - War (17)
    //## : A Lvn S War (11)
    //## : F NTH C Nwy - Yor (17)




}

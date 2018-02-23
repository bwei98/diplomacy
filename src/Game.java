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
        for(int id=0; id<orders.length; id++) {
            for(String o : orders[id]) {
                String[] order = o.split(" ");
                //order[0]=A or F, order[1]=start loc, order[2]=type 1, order[3]=dest, order[4]="-", order[5]=dest2
                for(String s: order)    s.trim();
                this.processOrder(id, order);
            }
        }
        //TODO returning
        return new Game();
    }

    /**
     * processOrder processes an order on a gamestate
     * @param id int representing country giving order
     * @param order String[] for the orders
     * @return boolean on if the order was valid
     */
    private boolean processOrder(int id, String[] order){
        //check if ownership is valid
        boolean unitCheck = true;
        Unit unit=new Unit();
        Territory loc = new Territory();

        //Ownership
        for (Unit u : this.countries[id].units) {
            if ((u.isFleet && order[0].equals("F") || !u.isFleet && order[0].equals("A"))
                    && (u.location.name.equals(order[1]))) {
                if (u.hasOrder) {
                    System.out.println(order[0] + " in " + order[1] + " received more than one order");
                    return false;
                }
                unit = u;
                loc = unit.location;
                unitCheck = false;
                unit.hasOrder = true;
                break;
            }
        }
        if (unitCheck) {
            System.out.println("Country " + Integer.toString(id)+ "does not own a " + order[0] + " in " + order[1]);
            return false;
        }

        //Hold
        if(order[2].equals("H")){
            for(Territory t: territories) {
                if (t.name.equals(order[1])) {
                    t.takeStrength[id]++;
                    break;
                }
            }
            return true;
        }

        //Support Hold
        if(order[2].equals("S") && order.length==4){
            if(unit.isFleet)
                for(Territory border : loc.neighborsF) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        border.takeStrength[border.occupied]++;
                        break;
                    }
                }
            else//unit is army
                for(Territory border : loc.neighborsA) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        border.takeStrength[border.occupied]++;
                        break;
                    }
                }
            //Not valid Support hold
            System.out.println("Not a valid support hold between " + order[1]+" and "+order[3] );
            return false;
        }

        //TODO Move
        //move is tricky because armies can move to non-adjacent territories through convoy
        //TODO Supported attacks

        //Convoy
        if(order[2].equals("C") && !unit.isFleet){
            System.out.println("Army in "+ order[1]+ " may not convoy");
            return false;
        }
        //TODO finish convoy

        System.out.println("Invalid entry format");
        return false;
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

public class Game {
    public Country[] countries;
    public Territory[] territories;
    //Countries know what units they have, units know where they are so that should be enough^

    /**
     * Basic constructor for a Game state
     * @param countries Country[] for what the countries are
     * @param territories Territory[] of all the territories
     */
    public Game(Country[] countries, Territory[] territories) {
        this.countries=countries;
        this.territories=territories;
    }



}

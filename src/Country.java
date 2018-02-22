public class Country {
    public String name;
    public Unit[] units;
    public Territory[] territories;

    /**
     * Basic constructor for Countries
     * @param n String for its name
     * @param u Unit[] for the units that Country controls
     * @param t Territory[] for the supply centers controlled
     */
    public Country(String n, Unit[] u, Territory[] t) {
        this.name = n;
        this.units = u;
        this.territories = t;
    }
}
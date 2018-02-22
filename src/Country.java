public class Country {
    public String name;
    public Unit[] units;
    public Territory[] territories;

    public Country(String n, Unit[] u, Territory[] t) {
        name = n;
        units = u;
        territories = t;
    }
}
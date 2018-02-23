public class Country {
    public String name;
    public Unit[] units;
    public Territory[] territories;
    public int id;

    /**
     * Basic constructor for Countries
     * @param name String for its name
     * @param units Unit[] for the units that Country controls
     * @param territories Territory[] for the supply centers controlled
     * @param id int for the country id
     */
    public Country(String name, Unit[] units, Territory[] territories, int id) {
        this.name = name;
        this.units = units;
        this.territories = territories;
        this.id = id;
    }

    /**
     * Default Country constructor
     */
    public Country(){
        name="";
        units=null;
        territories=null;
        id=-1;
    }

    /**
     * toString returns the name, units, and supply centers of a country
     */
    public String toString() {
        String str = name;
        str += "\nUnits: ";
        for(int i = 0; i < units.length; i++)
            str += units[i].toString() + " ";
        str += "\nSupply centers: ";
        for(int i = 0; i < territories.length; i++)
            if(territories[i].supplyCenter) str += territories[i].toString() + " ";
        str += "\n----------\n";
        return str;
    }

}
import org.jetbrains.annotations.NotNull;

public class Country implements Comparable {
    private final String name;
    private Unit[] units;
    private Territory[] supplyCenters;
    private final Territory[] homeSCs;
    private boolean alive;
    private final int id;

    /**
     * @return name of the country as string
     */
    public String getName() {
        return name;
    }

    /**
     * @param alive alive status to set (true = alive, false = not alive)
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * @return array of units owned by this country
     */
    public Unit[] getUnits() {
        return units;
    }

    /**
     * @return array of territories which are supply centers of this country
     */
    public Territory[] getSupplyCenters() {
        return supplyCenters;
    }

    /**
     * @return array of home supply centers of the country
     */
    public Territory[] getHomeSCs() {
        return homeSCs;
    }

    /**
     * @return are we alive?
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @return integer id of the country, this is a unique identifier for each country
     */
    public int getId() {
        return id;
    }

    /**
     * Basic constructor for Countries
     * @param name String for its name
     * @param units Unit[] for the units that Country controls
     * @param supplyCenters Territory[] for the supply centers controlled
     * @param id int for the country id
     */
    public Country(String name, @NotNull Unit[] units, @NotNull Territory[] supplyCenters, int id) {
        this.name = name;
        this.units = units;
        this.supplyCenters = supplyCenters;
        this.homeSCs = supplyCenters;
        this.alive = true;
        this.id = id;
    }

    /**
     * Actually useful constructor for Countries
     * @param name String for its name
     * @param supplyCenters Territory[] for the supply centers controlled
     * @param id int for the country id
     */
    public Country(String name, Territory[] supplyCenters, int id) {
        this.name = name;
        this.units = null;
        this.supplyCenters = supplyCenters;
        this.homeSCs = supplyCenters;
        this.alive = true;
        this.id = id;
    }

    /**
     * Default Country constructor
     */
    public Country(int cId){
        name="";
        units=null;
        supplyCenters =null;
        homeSCs = null;
        alive = false;
        id=cId;
    }

    /**
     * Set the country's units
     * @param units Unit[] for the country's new units
     */
    public void setUnits(Unit[] units) {
        this.units = units;
    }

    /**
     * Add a new territory
     * @param newSC The new territory the country gains
     */
    public void gainSupplyCenter(Territory newSC) {
        Territory[] newTerritories = new Territory[supplyCenters.length + 1];
        System.arraycopy(supplyCenters, 0, newTerritories, 0, supplyCenters.length);
        newTerritories[supplyCenters.length] = newSC;
        newSC.setSupplyCenter(id);

        supplyCenters = newTerritories;
    }

    /**
     * Remove the supply center from the country's SCs
     * Requires that the country actually has goneSC
     * @param goneSC The supply center to be removed
     */
    public void loseSupplyCenter(Territory goneSC) {
        Territory[] newTerritories = new Territory[supplyCenters.length - 1];
        int idx = 0;
        for(Territory territory : supplyCenters) {
            if(!territory.equals(goneSC)){
                newTerritories[idx] = territory;
                idx++;
            }
        }
        goneSC.setSupplyCenter(-1);

        supplyCenters = newTerritories;
    }

    /**
     * Check whether the country has the given supply center
     * @param scCheck The supply center to be checked
     * @return Whether or not the given supply center belongs to the country
     */
    public boolean hasSupplyCenter(Territory scCheck) {
        boolean hasSC = false;
        for(Territory sc : supplyCenters) {
            if (sc.equals(scCheck)) {
                hasSC = true;
                break;
            }
        }
        return hasSC;
    }

    /**
     * Create a unit and add it to the list of units the country controls
     * @param sc The territory we are building the unit in
     * @param isFleet Whether the unit is a fleet or not
     * Requires canBuild(sc)
     */
    public void build(Territory sc, boolean isFleet) {
        Unit unit = new Unit(this, isFleet, sc);
        Unit[] newUnits = new Unit[units.length + 1];
        System.arraycopy(units, 0, newUnits, 0, units.length);
        newUnits[units.length] = unit;
        sc.setOccupied(id);

        units = newUnits;
    }

    /**
     * Remove the given unit from the list of units the country controls
     * @param unit The unit we want to disband
     */
    public void disband(Unit unit) {
        Unit[] newUnits = new Unit[units.length - 1];
        int idx = 0;
        for(Unit u : units) {
            if(u != unit){
                newUnits[idx] = u;
                idx++;
            }
        }
        unit.getLocation().setOccupied(-1);

        units = newUnits;
    }

    /**
     * Calculate how many units a country needs to build or disband
     * Positive = disbands needed, negative = builds needed
     * @return see above
     */
    public int numBuildsOrDisbands() {
        int diff = supplyCenters.length - units.length;

        if(diff <= 0) return diff;
        else {
            int numBuilds = 0;
            for(Territory sc : supplyCenters) if(canBuild(sc)) numBuilds++;
            return Math.min(numBuilds, diff);
        }
    }

    /**
     * Check whether the country can build in the given supply center
     * @param sc The given supply center
     * @return Whether or not sc is a country's home supply center and is empty
     */
    public boolean canBuild(Territory sc) {
        for(Territory supplyCenter : supplyCenters) {
            if(sc.equals(supplyCenter) && sc.getOccupied() == -1) return true;
        }
        return false;
    }

    /**
     * Give country details
     * @return string with name, units, and supply centers of the country
     */
    @Override
    public String toString() {
        String str = name;
        str += "\nUnits: ";
        for (Unit unit : units) str += unit.toString();
        str += "\nSupply centers: ";
        for (Territory supplyCenter : supplyCenters)
            str += supplyCenter.toString() + " ";
        str += "\n----------\n";
        return str;
    }

    /**
     * compareTo provides an ordering on countries
     * @param o The other country
     * @return -1 if o has a smaller id and 1 if o has a larger id
     */
    @Override
    public int compareTo(Object o) {
        Country country2 = (Country)o;
        return country2.id - this.id;
    }

    /**
     * Check if two countries are equal
     * @param o - the other country
     * @return - are they equal?
     */
    @Override
    public boolean equals(Object o) {
        return this.compareTo(o) == 0;
    }


}
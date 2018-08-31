import java.util.ArrayList;

public class Unit implements Comparable {
    private final Country owner;
    private final boolean isFleet;    //true=fleet, false=army
    private Territory location;
    private boolean hasOrder;

    /**
     * Basic constructor for units
     * @param owner Country for who owns the unit
     * @param type boolean for type: true=fleet, false=army
     * @param init Territory for initial location
     */
    public Unit(Country owner, boolean type, Territory init) {
        this.owner = owner;
        this.isFleet = type;
        this.location = init;
        this.hasOrder=false;
    }


    public Unit(){
        owner=new Country();
        isFleet=false;
        location=new Territory();
        hasOrder=false;
    }

    /**
     * toString returns whether it is a fleet or an army, the location,
     * and the country that owns the unit
     */
    public String toString() {
        String str = "";
        if(isFleet) str += "F "; else str += "A ";
        str += location.toString() + " ";
        return str;
    }

    public Country getOwner() {
        return owner;
    }

    public boolean isFleet() {
        return isFleet;
    }

    public Territory getLocation() {
        return location;
    }

    public void setLocation(Territory location) {
        this.location = location;
    }

    public boolean hasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    /**

     * Get a list of territories a unit can move to (for use in retreat)
     * @return Territory array of open territories
     */
    public Territory[] canMove () {
        ArrayList<Territory> openTs = new ArrayList<>();
        if(isFleet) {
            for (Territory t : location.getNeighborsF()) if (t.getOccupied() != -1) openTs.add(t);
        } else {
            for (Territory t : location.getNeighborsA()) if (t.getOccupied() != -1) openTs.add(t);
        }

        return (Territory[])openTs.toArray();
    }

    /**
     * compareTo provides an ordering on Units
     * @param o The other Unit
     * @return a comparison based on owner first, then alphabetical
     */
    public int compareTo(Object o) {
        Unit unit2 = (Unit)o;
        if(owner.equals(unit2.owner)) return location.getName().compareTo(unit2.location.getName());
        else return owner.compareTo(unit2.owner);
    }

    /**
     * Moves unit from one territory to another
     * @param location2 Territory representing intended destination
     */
    public void move(Territory location2) {
        if(isFleet) {
            if(location.touchF(location2)) {
                location = location2;
                location.setOccupied(owner.getId());
            }
        } else {
            if(location.touchA(location2)) {
                location = location2;
                location.setOccupied(owner.getId());
            }
        }
    }



}

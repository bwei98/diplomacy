import java.util.ArrayList;

public class Unit implements Comparable {
    public Country owner;
    public boolean isFleet;    //true=fleet, false=army
    public Territory location;
    public boolean hasOrder;

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

    /**
     * Get a list of territories a unit can move to (for use in retreat)
     * @return Territory array of open territories
     */
    public Territory[] canMove () {
        ArrayList<Territory> openTs = new ArrayList<>();
        if(isFleet) {
            for (Territory t : location.neighborsF) if (t.occupied != -1) openTs.add(t);
        } else {
            for (Territory t : location.neighborsA) if (t.occupied != -1) openTs.add(t);
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
        if(owner.equals(unit2.owner)) return location.name.compareTo(unit2.location.name);
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
                location.occupied = owner.id;
            }
        } else {
            if(location.touchA(location2)) {
                location = location2;
                location.occupied = owner.id;
            }
        }
    }



}

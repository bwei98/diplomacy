public class Unit {
    public int owner;
    public boolean isFleet;    //true=fleet, false=army
    public Territory location;

    public void move(Territory location2) {
        if(isFleet) if(touchF(location2)) location = location2;
        else if(touchA(location2)) location = location2;
    }

    public Unit(int owner, boolean type, Territory init) {
        this.owner = owner;
        this.isFleet = type;
        this.location = init;
    }

}

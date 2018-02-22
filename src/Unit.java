public class Unit {
    public int owner;
    public boolean isFleet;    //true=fleet, false=army
    public Territory location;

    public Unit(int owner, boolean type, Territory init) {
        this.owner = owner;
        this.isFleet = type;
        this.location = init;
    }



}

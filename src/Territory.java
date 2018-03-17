import java.util.*;


public class Territory {
    public String name;
    public Territory[] neighborsF;
    public Territory[] neighborsA;
    public int occupied; //-1: unoccupied, otherwise country id
    public int supplyCenter; //-99 = no, -1 = yes and neutral, otherwise yes and country id
    public Integer[] takeStrength;

    /**
     * Basic constructor for territory
     * @param name string for the name
     * @param sc boolean for if it is a supplycenter
     */
    public Territory(String name, int sc) {
        this.name = name;
        this.neighborsF = new Territory[0];
        this.neighborsA = new Territory[0];
        this.supplyCenter = sc;
        this.occupied = -1;
        this.takeStrength = new Integer[Diplomacy.numCountries];
        Arrays.fill(takeStrength, 0);
    }

    /**
     * Default Territory constructor
     */
    public Territory(){
        name="";
        neighborsF=null;
        neighborsA=null;
        occupied=-1;
        supplyCenter=-99;
        takeStrength=null;
    }

    /**
     * Comparison function of Territories
     * @param T territory to which this is compared
     * @return boolean on if they are equal
     */
    public boolean equals(Territory T) {
        return (this.name).equals(T.name);
    }

    /**
     * Comparison function of Territory and string
     * @param s string representing territory to be compared
     * @return boolean on if they are equal
     */
    public boolean equals(String s){
        return this.name.equals(s);
    }

    /**
     * Check if territory touches w.r.t fleets
     * @param T 2nd territory to be compared
     * @return boolean which is true iff T touches w.r.t fleets
     */
    public boolean touchF(Territory T) {
        int len = this.neighborsF.length-1;
        while(len >= 0)
            if(this.neighborsF[len].equals(T))
                return true;
        return false;
    }

    /**
     * Check if territory touches w.r.t armies
     * @param T 2nd territory to be compared
     * @return boolean which is true iff T touches w.r.t armies
     */
    public boolean touchA(Territory T) {
        int len = this.neighborsF.length-1;
        while(len >= 0)
            if(this.neighborsA[len].equals(T))
                return true;
        return false;
    }

    /**
     * is this territory a supplyCenter?
     * @return is it?
     */
    public boolean isSupplyCenter() {
        return supplyCenter != -99;
    }

    /**
     * toString returns the name of the territory
     */
    public String toString() {
        return name;
    }

    /**
     * Computes all Neighbors of a territory
     * @return Territory[] containing all neighbors of the Territory
     */
    public Territory[] allNeighbors() {
        Set<Territory> union = new HashSet<Territory>();
        union.addAll(Arrays.asList(neighborsA));
        union.addAll(Arrays.asList(neighborsF));

        return union.toArray(new Territory[union.size()]);
    }

    /**
     * Set the neighbors of the Territory
     * @param neighbors The territory neighbors
     * @param isFleet Whether the territories are connected by land or water
     * @return
     */
    public void setNeighbors(Territory[] neighbors, boolean isFleet) {
        if(isFleet) neighborsF = neighbors;
        else neighborsA = neighbors;
    }

    /**
     * Set the neighbors of the Territory
     * @param landNeighbors The territory's land connected neighbors
     * @param seaNeighbors The territory's sea connected neighbors
     */
    public void setNeighbors(Territory[] landNeighbors, Territory[] seaNeighbors) {
        neighborsA = landNeighbors;
        neighborsF = seaNeighbors;
    }

    /**
     * Is the territory landlocked
     * @return boolean of if a fleet can reach the territory
     */
    public boolean landlocked(){
        return this.neighborsF.length==0;
    }

    /**
     * Is the territory open water
     * @return boolean of if an army can reach it
     */
    public boolean water(){
        return this.neighborsA.length==0;
    }

    /**
     * is the territory coast
     * @return boolean of if both armies and fleets can reach
     */
    public boolean coast(){
        return !this.water() && !this.landlocked();
    }
}

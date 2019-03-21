import org.apache.commons.lang3.tuple.MutableTriple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Territory {
    private final String name;
    private Territory[] neighborsF;
    private Territory[] neighborsA;
    private int occupied; //-1: unoccupied, otherwise country id
    private int supplyCenter; //-99 = no, -1 = yes and neutral, otherwise yes and country id
    private Integer[] takeStrength;
    private ArrayList<Territory> pair;

    private ArrayList<MutableTriple<Move, Country, Integer>> attacks;

    /*
    Here's a bunch of getters and setters, they are pretty obvious, you know what they do.
     */
    public String getName() {
        return name;
    }

    public Territory[] getNeighborsF() {
        return neighborsF;
    }

    public void setNeighborsF(Territory[] neighborsF) {
        this.neighborsF = neighborsF;
    }

    public Territory[] getNeighborsA() {
        return neighborsA;
    }

    public void setNeighborsA(Territory[] neighborsA) {
        this.neighborsA = neighborsA;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public int getSupplyCenter() {
        return supplyCenter;
    }

    public void setSupplyCenter(int supplyCenter) {
        this.supplyCenter = supplyCenter;
    }

    public Integer[] getTakeStrength() {
        return takeStrength;
    }

    public void setTakeStrength(Integer[] takeStrength) {
        this.takeStrength = takeStrength;
    }

    public ArrayList<Territory> getPair() {
        return pair;
    }

    public void setPair(ArrayList<Territory> pair) {
        this.pair = pair;
    }

    public ArrayList<MutableTriple<Move, Country, Integer>> getAttacks() {
        return attacks;
    }

    public void setAttacks(ArrayList<MutableTriple<Move, Country, Integer>> attacks) {
        this.attacks = attacks;
    }



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
        this.attacks=new ArrayList<>();
        this.pair=null;
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
        attacks=null;
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
        return this.name.toUpperCase().equals(s.toUpperCase());
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
     * Adds a territory which is paired
     * @param t the territory to add
     * @return the original territory
     */
    public Territory setPair(Territory t){
        this.pair.add(t);
        return this;
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
        Set<Territory> union = new HashSet<>();
        union.addAll(Arrays.asList(neighborsA));
        union.addAll(Arrays.asList(neighborsF));

        return union.toArray(new Territory[union.size()]);
    }

    /**
     * Set the neighbors of the Territory
     * @param neighbors The territory neighbors
     * @param isFleet Whether the territories are connected by land or water
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


public class Territory{
	public String name;
	public Territory[] neighborsF;
	public Territory[] neighborsA;
	public int occupied; //0: unoccupied, 1: army, -1: fleet
	public boolean supplyCenter; //true=yes

    public int holdStrength;
    public int[] attackStrength;

    /**
     * Basic constructor for territory
     * @param name string for the name
     * @param nF Territory[] for neighbors w.r.t. fleets
     * @param nA Territory[] for neighbors w.r.t. armies
     * @param occ int for if it is occupied, 0: unoccupied, 1: army, -1: fleet
     * @param sc boolean for if it is a supplycenter
     */
	public Territory(String name, Territory[] nF, Territory[] nA, int occ, boolean sc) {
        this.name = name;
        this.neighborsF = nF;
        this.neighborsA = nA;
        this.occupied = occ;
        this.supplyCenter = sc;

        this.holdStrength = 0;
        this.attackStrength = new int[Diplomacy.numCountries];
    }

    /**
     * Comparison function of Territories
     * @param T territory to which this is compared
     * @return boolean on if they are equal
     */
    public boolean equals(Territory T) {
	    return this.name.equals(T.name);
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



}

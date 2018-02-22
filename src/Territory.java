
public class Territory{
	public String name;
	public Territory[] neighborsF;
	public Territory[] neighborsA;
	public int occupied; //0: unoccupied, 1: army, -1: fleet
	public boolean supplyCenter; //true=yes

	public Territory(String name, Territory[] nF, Territory[] nA, int occ, boolean sc) {
        this.name = name;
        this.neighborsF = nF;
        this.neighborsA = nA;
        this.occupied = occ;
        this.supplyCenter = sc;
    }

    public boolean equals(Territory T) {
	    return this.name.equals(T.name);
    }

    public boolean touchF(Territory T) {
	    int len = this.neighborsF.length-1;
	    while(len >= 0)
            if(this.neighborsF[len].equals(T))
                return true;
	    return false;
    }

    public boolean touchA(Territory T) {
        int len = this.neighborsF.length-1;
        while(len >= 0)
            if(this.neighborsA[len].equals(T))
                return true;
        return false;
    }



}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Map {
    public static final int SPRING = 1;
    public static final int FALL = 2;
    public static final int WINTER = 0;

    public static final int NUM_TERRITORIES = 6; //TODO make this 75
    public static final Territory[] TERRITORIES = new Territory[NUM_TERRITORIES];

    private static Territory Lvp = new Territory("Lvp", true);
    private static Territory Cly = new Territory("Cly", true);
    private static Territory Yor = new Territory("Yor", true);
    private static Territory Wal = new Territory("Wal", true);
    private static Territory Edi = new Territory("Edi", true);
    private static Territory Lon = new Territory("Lon", true);

    public static void initMap() {
        TERRITORIES[0] = Lvp;
        TERRITORIES[1] = Cly;
        TERRITORIES[2] = Yor;
        TERRITORIES[3] = Wal;
        TERRITORIES[4] = Edi;
        TERRITORIES[5] = Lon;

        Lvp.setNeighbors(new Territory[]{Cly, Yor, Wal, Edi, Lon}, new Territory[]{Cly, Wal});
        Cly.setNeighbors(new Territory[]{Edi, Lvp}, new Territory[]{Edi, Lvp});
        Yor.setNeighbors(new Territory[]{Lvp, Wal, Edi, Lon}, new Territory[]{Edi, Lon});
        Wal.setNeighbors(new Territory[]{Lvp, Yor, Lon}, new Territory[]{Lvp, Lon});
        Edi.setNeighbors(new Territory[]{Cly, Lvp, Lon}, new Territory[]{Cly, Yor});
        Lon.setNeighbors(new Territory[]{Yor, Wal}, new Territory[]{Yor, Wal});
    }

    /**
     * Set t1 and t2 as neighbors of each other
     p* @param t1 Territory 1
     * @param t2 Territory 2
     * @param canFleet Whether they are connected by sea
     * @param canArmy Whether they are connected by land
     */
    public static void makeNeighbor(Territory t1, Territory t2, boolean canFleet, boolean canArmy) {
        if(canFleet) {
            List<Territory> neighborsT1 = Arrays.asList(t1.neighborsF);
            List<Territory> neighborsT2 = Arrays.asList(t2.neighborsF);
            neighborsT1.add(t2);
            neighborsT2.add(t1);
            t1.neighborsF = (Territory[])neighborsT1.toArray();
            t2.neighborsF = (Territory[])neighborsT2.toArray();
        }
        if(canArmy) {
            List<Territory> neighborsT1 = Arrays.asList(t1.neighborsA);
            List<Territory> neighborsT2 = Arrays.asList(t2.neighborsA);
            neighborsT1.add(t2);
            neighborsT2.add(t1);
            t1.neighborsA = (Territory[])neighborsT1.toArray();
            t2.neighborsA = (Territory[])neighborsT2.toArray();
        }
    }
}
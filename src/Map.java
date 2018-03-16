import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Map {
    public static final int SPRING = 1;
    public static final int FALL = 2;
    public static final int WINTER = 0;

    public static final int NUM_TERRITORIES = 75; //Change to 6 for UK
    public static final Territory[] TERRITORIES = new Territory[NUM_TERRITORIES];

    public static Territory Lvp = new Territory("Lvp", true);
    public static Territory Cly = new Territory("Cly", false);
    public static Territory Yor = new Territory("Yor", false);
    public static Territory Wal = new Territory("Wal", false);
    public static Territory Edi = new Territory("Edi", true);
    public static Territory Lon = new Territory("Lon", true);
    public static Territory Por = new Territory("Por", true);
    public static Territory Spa = new Territory("Spa", true);
    public static Territory Gas = new Territory("Gas", false);
    public static Territory Mar = new Territory("Mar", true);

    public static Territory Par = new Territory("Par", true);
    public static Territory Bre = new Territory("Bre", true);
    public static Territory Pic = new Territory("Pic", false);
    public static Territory Bur = new Territory("Bur", false);
    public static Territory Bel = new Territory("Bel", true);
    public static Territory Hol = new Territory("Hol", true);
    public static Territory Ruh = new Territory("Ruh", true);
    public static Territory Kie = new Territory("Kie", true);
    public static Territory Mun = new Territory("Mun", true);
    public static Territory Ber = new Territory("Ber", true);

    public static Territory Den = new Territory("Den", true);
    public static Territory Pie = new Territory("Pie", false);
    public static Territory Tyr = new Territory("Tyr", false);
    public static Territory Boh = new Territory("Boh", false);
    public static Territory Pru = new Territory("Pru", false);
    public static Territory Sil = new Territory("Sil", false);
    public static Territory War = new Territory("War", true);
    public static Territory Gal = new Territory("Gal", false);
    public static Territory Lvn = new Territory("Lvn", false);
    public static Territory Stp = new Territory("Stp", true);

    public static Territory Mos = new Territory("Mos", true);
    public static Territory Ukr = new Territory("Ukr", false);
    public static Territory Sev = new Territory("Sev", true);
    public static Territory Rum = new Territory("Rum", true);
    public static Territory Vie = new Territory("Vie", true);
    public static Territory Bud = new Territory("Bud", true);
    public static Territory Tri = new Territory("Tri", true);
    public static Territory Ven = new Territory("Ven", true);
    public static Territory Tus = new Territory("Tus", false);
    public static Territory Rom = new Territory("Rom", true);

    public static Territory Apu = new Territory("Apu", false);
    public static Territory Nap = new Territory("Nap", true);
    public static Territory Ser = new Territory("Ser", true);
    public static Territory Alb = new Territory("Alb", false);
    public static Territory Gre = new Territory("Gre", true);
    public static Territory Bul = new Territory("Bul", true);
    public static Territory Con = new Territory("Con", true);
    public static Territory Smy = new Territory("Smy", true);
    public static Territory Ank = new Territory("Ank", true);
    public static Territory Arm = new Territory("Arm", false);

    public static Territory Syr = new Territory("Syr", false);
    public static Territory Naf = new Territory("Naf", false);
    public static Territory Tun = new Territory("Tun", true);
    public static Territory Nwy = new Territory("Nwy", true);
    public static Territory Swe = new Territory("Swe", true);
    public static Territory Fin = new Territory("Fin", false);

    public static Territory BAR = new Territory("BAR", false);
    public static Territory NWG = new Territory("NWG", false);
    public static Territory BOT = new Territory("BOT", false);
    public static Territory BAL = new Territory("BAL", false);
    public static Territory SKA = new Territory("SKA", false);
    public static Territory HEL = new Territory("HEL", false);
    public static Territory NTH = new Territory("NTH", false);
    public static Territory NAO = new Territory("NAO", false);
    public static Territory IRI = new Territory("IRI", false);
    public static Territory ENG = new Territory("ENG", false);

    public static Territory MAO = new Territory("MAO", false);
    public static Territory LYO = new Territory("LYO", false);
    public static Territory WES = new Territory("WES", false);
    public static Territory TYS = new Territory("TYS", false);
    public static Territory ADR = new Territory("ADR", false);
    public static Territory ION = new Territory("ION", false);
    public static Territory AEG = new Territory("AEG", false);
    public static Territory EAS = new Territory("EAS", false);
    public static Territory BLA = new Territory("BLA", false);


    public static void initUK() {
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


    public static void initFull(){
        TERRITORIES[0] = Lvp;   TERRITORIES[10] = Par;  TERRITORIES[20] = Den;  TERRITORIES[30] = Mos;
        TERRITORIES[1] = Cly;   TERRITORIES[11] = Bre;  TERRITORIES[21] = Pie;  TERRITORIES[31] = Ukr;
        TERRITORIES[2] = Yor;   TERRITORIES[12] = Pic;  TERRITORIES[22] = Tyr;  TERRITORIES[32] = Sev;
        TERRITORIES[3] = Wal;   TERRITORIES[13] = Bur;  TERRITORIES[23] = Boh;  TERRITORIES[33] = Rum;
        TERRITORIES[4] = Edi;   TERRITORIES[14] = Bel;  TERRITORIES[24] = Pru;  TERRITORIES[34] = Vie;
        TERRITORIES[5] = Lon;   TERRITORIES[15] = Hol;  TERRITORIES[25] = Sil;  TERRITORIES[35] = Bud;
        TERRITORIES[6] = Por;   TERRITORIES[16] = Ruh;  TERRITORIES[26] = War;  TERRITORIES[36] = Tri;
        TERRITORIES[7] = Spa;   TERRITORIES[17] = Kie;  TERRITORIES[27] = Gal;  TERRITORIES[37] = Ven;
        TERRITORIES[8] = Gas;   TERRITORIES[18] = Mun;  TERRITORIES[28] = Lvn;  TERRITORIES[38] = Tus;
        TERRITORIES[9] = Mar;   TERRITORIES[19] = Ber;  TERRITORIES[29] = Stp;  TERRITORIES[39] = Rom;

        TERRITORIES[40] = Apu;  TERRITORIES[50] = Syr;  TERRITORIES[56] = BAR;  TERRITORIES[66] = MAO;
        TERRITORIES[41] = Nap;  TERRITORIES[51] = Naf;  TERRITORIES[57] = NWG;  TERRITORIES[67] = LYO;
        TERRITORIES[42] = Ser;  TERRITORIES[52] = Tun;  TERRITORIES[58] = BOT;  TERRITORIES[68] = WES;
        TERRITORIES[43] = Alb;  TERRITORIES[53] = Nwy;  TERRITORIES[59] = BAL;  TERRITORIES[69] = TYS;
        TERRITORIES[44] = Gre;  TERRITORIES[54] = Swe;  TERRITORIES[60] = SKA;  TERRITORIES[70] = ADR;
        TERRITORIES[45] = Bul;  TERRITORIES[55] = Fin;  TERRITORIES[61] = HEL;  TERRITORIES[71] = ION;
        TERRITORIES[46] = Con;                          TERRITORIES[62] = NTH;  TERRITORIES[72] = AEG;
        TERRITORIES[47] = Smy;                          TERRITORIES[63] = NAO;  TERRITORIES[73] = EAS;
        TERRITORIES[48] = Ank;                          TERRITORIES[64] = IRI;  TERRITORIES[74] = BLA;
        TERRITORIES[49] = Arm;                          TERRITORIES[65] = ENG;

        Lvp.setNeighbors(new Territory[]{Cly, Yor, Wal, Edi, Lon}, new Territory[]{Cly, Wal, NAO, IRI});
        Cly.setNeighbors(new Territory[]{Edi, Lvp}, new Territory[]{Edi, Lvp, NAO, NWG});
        Yor.setNeighbors(new Territory[]{Lvp, Wal, Edi, Lon}, new Territory[]{Edi, Lon, NTH});
        Wal.setNeighbors(new Territory[]{Lvp, Yor, Lon}, new Territory[]{Lvp, Lon, IRI, ENG});
        Edi.setNeighbors(new Territory[]{Cly, Lvp, Lon}, new Territory[]{Cly, Yor, NWG, NTH});
        Lon.setNeighbors(new Territory[]{Yor, Wal}, new Territory[]{Yor, Wal, ENG, NTH});
        Por.setNeighbors(new Territory[]{Spa}, new Territory[]{MAO, Spa});
        Spa.setNeighbors(new Territory[]{Por, Gas, Mar}, new Territory[]{MAO, Por, LYO, WES, Mar, Gas});
        Gas.setNeighbors(new Territory[]{Bre, Par, Bur, Mar, Spa}, new Territory[]{MAO, Bre, Spa});
        Mar.setNeighbors(new Territory[]{Spa, Gas, Bur, Pie}, new Territory[]{Pie, Spa, LYO});

        Par.setNeighbors(new Territory[]{Pic, Bre, Gas, Bur, Mar}, new Territory[]{});
        Bre.setNeighbors(new Territory[]{Pic, Par, Gas}, new Territory[]{Pic, Gas, MAO, ENG});
        Pic.setNeighbors(new Territory[]{Par, Bre, Bel, Bur}, new Territory[]{ENG, Bre, Bel});
        Bur.setNeighbors(new Territory[]{Gas, Par, Pic, Bel, Ruh, Mun, Mar}, new Territory[]{});
        Bel.setNeighbors(new Territory[]{Pic, Bur, Ruh, Hol}, new Territory[]{ENG, NTH, Pic, Hol});
        Hol.setNeighbors(new Territory[]{Bel, Ruh, Kie}, new Territory[]{HEL, NTH, Kie, Bel});
        Ruh.setNeighbors(new Territory[]{Bel, Hol, Kie, Mun, Bur}, new Territory[]{});
        Kie.setNeighbors(new Territory[]{Den, Hol, Ber, Mun, Ruh}, new Territory[]{HEL, BAL, Hol, Den, Ber});
        Mun.setNeighbors(new Territory[]{Kie, Ber, Sil, Boh, Tyr, Bur, Ruh}, new Territory[]{});
        Ber.setNeighbors(new Territory[]{Mun, Kie, Pru, Sil}, new Territory[]{Pru, BAL, Kie});
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
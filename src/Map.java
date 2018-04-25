import java.util.Arrays;
import java.util.List;

public class Map {
    public static final int SPRING = 1;
    public static final int FALL = 2;
    public static final int WINTER = 0;

    public static final int NUM_TERRITORIES = 75; //Change to 6 for UK
    public static final Territory[] TERRITORIES = new Territory[NUM_TERRITORIES];

    //Countries:
    //England 0, France 1, Germany 2, Russia 3, Austria 4, Italy 5, Trukey 6

    public static Territory Lvp = new Territory("Lvp", 0);
    public static Territory Cly = new Territory("Cly", -99);
    public static Territory Yor = new Territory("Yor", -99);
    public static Territory Wal = new Territory("Wal", -99);
    public static Territory Edi = new Territory("Edi", 0);
    public static Territory Lon = new Territory("Lon", 0);
    public static Territory Por = new Territory("Por", -1);
    public static Territory Spa = new Territory("Spa", -1);
    public static Territory Gas = new Territory("Gas", -99);
    public static Territory Mar = new Territory("Mar", 1);

    public static Territory Par = new Territory("Par", 1);
    public static Territory Bre = new Territory("Bre", 1);
    public static Territory Pic = new Territory("Pic", -99);
    public static Territory Bur = new Territory("Bur", -99);
    public static Territory Bel = new Territory("Bel", -1);
    public static Territory Hol = new Territory("Hol", -1);
    public static Territory Ruh = new Territory("Ruh", -99);
    public static Territory Kie = new Territory("Kie", 2);
    public static Territory Mun = new Territory("Mun", 2);
    public static Territory Ber = new Territory("Ber", 2);

    public static Territory Den = new Territory("Den", -1);
    public static Territory Pie = new Territory("Pie", -99);
    public static Territory Tyr = new Territory("Tyr", -99);
    public static Territory Boh = new Territory("Boh", -99);
    public static Territory Pru = new Territory("Pru", -99);
    public static Territory Sil = new Territory("Sil", -99);
    public static Territory War = new Territory("War", 3);
    public static Territory Gal = new Territory("Gal", -99);
    public static Territory Lvn = new Territory("Lvn", -99);
    public static Territory Stp = new Territory("Stp", 3);

    public static Territory Mos = new Territory("Mos", 3);
    public static Territory Ukr = new Territory("Ukr", -99);
    public static Territory Sev = new Territory("Sev", 3);
    public static Territory Rum = new Territory("Rum", -1);
    public static Territory Vie = new Territory("Vie", 4);
    public static Territory Bud = new Territory("Bud", 4);
    public static Territory Tri = new Territory("Tri", 4);
    public static Territory Ven = new Territory("Ven", 5);
    public static Territory Tus = new Territory("Tus", -99);
    public static Territory Rom = new Territory("Rom", 5);

    public static Territory Apu = new Territory("Apu", -99);
    public static Territory Nap = new Territory("Nap", 5);
    public static Territory Ser = new Territory("Ser", -1);
    public static Territory Alb = new Territory("Alb", -99);
    public static Territory Gre = new Territory("Gre", -1);
    public static Territory Bul = new Territory("Bul", -1);
    public static Territory Con = new Territory("Con", 6);
    public static Territory Smy = new Territory("Smy", 6);
    public static Territory Ank = new Territory("Ank", 6);
    public static Territory Arm = new Territory("Arm", -99);

    public static Territory Syr = new Territory("Syr", -99);
    public static Territory Naf = new Territory("Naf", -99);
    public static Territory Tun = new Territory("Tun", -1);
    public static Territory Nwy = new Territory("Nwy", -1);
    public static Territory Swe = new Territory("Swe", -1);
    public static Territory Fin = new Territory("Fin", -99);

    public static Territory BAR = new Territory("BAR", -99);
    public static Territory NWG = new Territory("NWG", -99);
    public static Territory BOT = new Territory("BOT", -99);
    public static Territory BAL = new Territory("BAL", -99);
    public static Territory SKA = new Territory("SKA", -99);
    public static Territory HEL = new Territory("HEL", -99);
    public static Territory NTH = new Territory("NTH", -99);
    public static Territory NAO = new Territory("NAO", -99);
    public static Territory IRI = new Territory("IRI", -99);
    public static Territory ENG = new Territory("ENG", -99);

    public static Territory MAO = new Territory("MAO", -99);
    public static Territory LYO = new Territory("LYO", -99);
    public static Territory WES = new Territory("WES", -99);
    public static Territory TYS = new Territory("TYS", -99);
    public static Territory ADR = new Territory("ADR", -99);
    public static Territory ION = new Territory("ION", -99);
    public static Territory AEG = new Territory("AEG", -99);
    public static Territory EAS = new Territory("EAS", -99);
    public static Territory BLA = new Territory("BLA", -99);


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

        Den.setNeighbors(new Territory[]{Swe, Kie}, new Territory[]{NTH, SKA, BAL, HEL, Kie, Swe});
        Pie.setNeighbors(new Territory[]{Mar, Tus, Tyr, Ven}, new Territory[]{Mar, Tus, LYO});
        Tyr.setNeighbors(new Territory[]{Pie, Ven, Tri, Vie, Boh, Mun}, new Territory[]{});
        Boh.setNeighbors(new Territory[]{Mun, Sil, Gal, Vie, Tyr}, new Territory[]{});
        Pru.setNeighbors(new Territory[]{Ber, Sil, War, Lvn}, new Territory[]{Ber, BAL, Lvn});
        Sil.setNeighbors(new Territory[]{Mun, Ber, Pru, War, Gal, Boh}, new Territory[]{});
        War.setNeighbors(new Territory[]{Sil, Pru, Lvn, Mos, Ukr, Gal}, new Territory[]{});
        Gal.setNeighbors(new Territory[]{Boh, Sil, War, Ukr, Rum, Bud, Vie}, new Territory[]{});
        Lvn.setNeighbors(new Territory[]{Pru, War, Stp, Mos}, new Territory[]{BAL, BOT, Stp, Pru});
        Stp.setNeighbors(new Territory[]{Nwy, Fin, Lvn, Mos}, new Territory[]{BAR, Nwy, Fin, Lvn, BOT});

        Mos.setNeighbors(new Territory[]{Stp, Lvn, War, Ukr, Sev}, new Territory[]{});
        Ukr.setNeighbors(new Territory[]{War, Mos, Sev, Rum, Gal}, new Territory[]{});
        Sev.setNeighbors(new Territory[]{Mos, Ukr, Rum, Arm}, new Territory[]{Rum, BLA, Arm});
        Rum.setNeighbors(new Territory[]{Sev, Ukr, Gal, Bud, Ser, Bul}, new Territory[]{Sev, Bul, BLA});
        Vie.setNeighbors(new Territory[]{Boh, Gal, Bud, Tri, Tyr}, new Territory[]{});
        Bud.setNeighbors(new Territory[]{Vie, Gal, Rum, Ser, Tri}, new Territory[]{});
        Tri.setNeighbors(new Territory[]{Ven, Tyr, Vie, Bud, Ser, Alb}, new Territory[]{Ven, ADR, Alb});
        Ven.setNeighbors(new Territory[]{Pie, Tus, Tyr, Tri, Apu, Rom}, new Territory[]{Tri, Apu, ADR});
        Tus.setNeighbors(new Territory[]{Pie, Ven, Rom}, new Territory[]{Pie, LYO, Rom, TYS});
        Rom.setNeighbors(new Territory[]{Tus, Ven, Apu, Nap}, new Territory[]{Tus, Nap, TYS});

        Apu.setNeighbors(new Territory[]{Ven, Rom, Nap}, new Territory[]{Ven, ADR, ION, Nap});
        Nap.setNeighbors(new Territory[]{Apu, Rom}, new Territory[]{Rom, Apu, TYS, ION});
        Ser.setNeighbors(new Territory[]{Tri, Bud, Rum, Bul, Gre, Alb}, new Territory[]{});
        Alb.setNeighbors(new Territory[]{Tri, Ser, Gre}, new Territory[]{Tri, Gre, ION, ADR});
        Gre.setNeighbors(new Territory[]{Alb, Ser, Bul}, new Territory[]{Alb, Bul, AEG, ION});
        Bul.setNeighbors(new Territory[]{Gre, Ser, Rum, Con}, new Territory[]{Gre, AEG, Con, Rum, BLA});
        Con.setNeighbors(new Territory[]{Bul, Ank, Smy}, new Territory[]{BLA, Ank, AEG, Bul, Smy});
        Smy.setNeighbors(new Territory[]{Con, Ank, Arm, Syr}, new Territory[]{AEG, Con, Syr, EAS});
        Ank.setNeighbors(new Territory[]{Con, Smy, Arm}, new Territory[]{Arm, Con, BLA});
        Arm.setNeighbors(new Territory[]{Ank, Smy, Syr, Sev}, new Territory[]{Ank, Sev, BLA});

        Syr.setNeighbors(new Territory[]{Smy, Arm}, new Territory[]{Smy, EAS});
        Naf.setNeighbors(new Territory[]{Tun}, new Territory[]{MAO, WES, Tun});
        Tun.setNeighbors(new Territory[]{Naf}, new Territory[]{ION, TYS, WES, Naf});
        Nwy.setNeighbors(new Territory[]{Swe, Fin, Stp}, new Territory[]{Stp, BAR, NWG, NTH, SKA, Swe});
        Swe.setNeighbors(new Territory[]{Nwy, Den, Fin}, new Territory[]{BOT, Fin, BAL, Den, SKA, Nwy});
        Fin.setNeighbors(new Territory[]{Nwy, Swe, Stp}, new Territory[]{BOT, Stp, Swe});

        BAR.setNeighbors(new Territory[]{}, new Territory[]{Stp, Nwy, NWG});
        NWG.setNeighbors(new Territory[]{}, new Territory[]{NAO, Cly, Edi, NTH, Nwy, BAR});
        BOT.setNeighbors(new Territory[]{}, new Territory[]{Swe, Fin, Stp, Lvn, BAL});
        BAL.setNeighbors(new Territory[]{}, new Territory[]{Den, Swe, BOT, Lvn, Pru, Ber, Kie});
        SKA.setNeighbors(new Territory[]{}, new Territory[]{NTH, Nwy, Swe, Den});
        HEL.setNeighbors(new Territory[]{}, new Territory[]{NTH, Den, Kie, Hol});
        NTH.setNeighbors(new Territory[]{}, new Territory[]{Lon, Yor, Edi, NWG, Nwy, SKA, Den, HEL, Hol, Bel, ENG});
        NAO.setNeighbors(new Territory[]{}, new Territory[]{NWG, Cly, Lvp, IRI, MAO});
        IRI.setNeighbors(new Territory[]{}, new Territory[]{NAO, Cly, Lvp, Wal, ENG, MAO});
        ENG.setNeighbors(new Territory[]{}, new Territory[]{MAO, IRI, Wal, Lon, Bel, Pic, Bre});

        MAO.setNeighbors(new Territory[]{}, new Territory[]{NAO, IRI, ENG, Bre, Gas, Spa, Por, Naf, WES});
        LYO.setNeighbors(new Territory[]{}, new Territory[]{Spa, Mar, Pie, Tus, TYS, WES});
        WES.setNeighbors(new Territory[]{}, new Territory[]{MAO, Spa, LYO, TYS, Tun, Naf});
        TYS.setNeighbors(new Territory[]{}, new Territory[]{LYO, Tus, Rom, Nap, ION, Tun, WES});
        ADR.setNeighbors(new Territory[]{}, new Territory[]{Apu, Ven, Tri, Alb, ION});
        ION.setNeighbors(new Territory[]{}, new Territory[]{Tun, TYS, Nap, Apu, ADR, Alb, Gre, AEG, EAS});
        AEG.setNeighbors(new Territory[]{}, new Territory[]{ION, Gre, Bul, Con, Smy, EAS});
        EAS.setNeighbors(new Territory[]{}, new Territory[]{ION, AEG, Smy, Syr});
        BLA.setNeighbors(new Territory[]{}, new Territory[]{Con, Bul, Rum, Sev, Arm, Ank});
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
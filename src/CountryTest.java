import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


class CountryTest {
    private static Territory T1, T2, T3;
    private static Unit U1, U2;
    private static Country C;
    private static Unit[] units;
    private static Territory[] scs;

    @BeforeEach
    void initEach() {
        T1 = new Territory("terr1", -1);
        T2 = new Territory("terr2", 0);
        T3 = new Territory("terr3", 0);

        C = new Country();
        U1 = new Unit(C, true, T2);
        U2 = new Unit(C, false, T3);
        Unit[] u = {U1, U2};
        units = u;
        Territory[] t = {T2, T3};
        scs = t;
        C = new Country("CountryA", units, scs, 0);
    }

    @org.junit.jupiter.api.Test
    void getName() {
        Assertions.assertEquals(C.getName(), "CountryA", "Fails to get correct name");
    }

    @org.junit.jupiter.api.Test
    void setAlive() {
        Assertions.assertTrue(C.isAlive(), "Constructor should initialize to isAlive");
        C.setAlive(false);
        Assertions.assertFalse(C.isAlive(), "setAlive fails");
    }

    @org.junit.jupiter.api.Test
    void getUnits() {
        Unit[] u = C.getUnits();
        Assertions.assertArrayEquals(u, units, "getUnits fails to get the units");
    }


    @org.junit.jupiter.api.Test
    void numBuildsOrDisbands() {
        Assertions.assertEquals(C.numBuildsOrDisbands(), 0,
                "fails to compute number of disbands/builds after init");
    }

    @org.junit.jupiter.api.Test
    void hasSupplyCenter() {
        Assertions.assertTrue(C.hasSupplyCenter(T2), "fails hasSupplyCenter when true");
        Assertions.assertFalse(!C.hasSupplyCenter(T1), "fails hasSupplyCenter when false");
    }

    @org.junit.jupiter.api.Test
    void gainSupplyCenter() {
        C.gainSupplyCenter(T1);
        Assertions.assertEquals(T1.getSupplyCenter(),0, "territory sc owner not updated on gain");
        Assertions.assertTrue(C.hasSupplyCenter(T1), "Country does not have sc added");
        Assertions.assertTrue(C.hasSupplyCenter(T3), "Country accidentally changes other sc's");
        Assertions.assertEquals(C.numBuildsOrDisbands(),-1,
                "fail compute number of disbands/builds after gaining sc");
    }

    @org.junit.jupiter.api.Test
    void loseSupplyCenter() {
        C.loseSupplyCenter(T2);
        Assertions.assertNotEquals(T2.getSupplyCenter(), 0, "territory sc owner not updated on loss");
        Assertions.assertFalse(C.hasSupplyCenter(T2), "Country does not have sc lost");
        Assertions.assertTrue(C.hasSupplyCenter(T3), "Country accidentally changes other sc's");
        Assertions.assertEquals(C.numBuildsOrDisbands(),1,
                "fail compute number of disbands/builds after gaining sc");
        Assertions.assertEquals(C.getSupplyCenters().length,1, "sc array incorrect after loss");
    }

    @org.junit.jupiter.api.Test
    void canBuild() {
        Territory[] T = {T1, T2, T3};
        for(Territory t : T)
            Assertions.assertFalse(C.canBuild(t), "canBuild in territory when cannot");
        C.gainSupplyCenter(T1);
        Assertions.assertTrue(C.canBuild(T1), "canBuild fail after gain sc");
        C.loseSupplyCenter(T2);
        Assertions.assertFalse(C.canBuild(T2), "canBuild fail after lose sc");

    }

    @org.junit.jupiter.api.Test
    void build() {
        C.gainSupplyCenter(T1);
        C.build(T1, true);
        Assertions.assertTrue(C.hasSupplyCenter(T1), "build interfered with sc's");
        Assertions.assertEquals(C.getUnits().length, 3, "build failed");
    }

    @org.junit.jupiter.api.Test
    void disband() {
        C.loseSupplyCenter(T2);
        C.disband(U1);
        Assertions.assertEquals(C.getUnits().length, 1, "disband failed");
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        Country C2 = new Country("Country2", null, 0);
        Assertions.assertEquals(C2.compareTo(C), 0, "compareTo fail when equal");
    }

    @org.junit.jupiter.api.Test
    void equals() {
        Country C2 = new Country("Country2", null, 0);
        Assertions.assertEquals(C, C2, "equals fail when equal");
        C2 = new Country("Country2", null, 1);
        Assertions.assertNotEquals(C, C2, "equals fail when not equal");
    }
}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

enum Type                                                                                                              {
    D, H, M, C, CM, SH, SA                                                                                             }
enum Status                                                                                                            {
    EXECUTED, EXECUTABLE, PENDING, UNREAD, FAILED                                                                      }

public class Move implements Comparable {
    private Country country;
    private Unit unit;
    private Territory destination;
    private Territory source;
    private Type type;
    private Status status;


    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Territory getDestination() {
        return destination;
    }

    public void setDestination(Territory destination) {
        this.destination = destination;
    }

    public Territory getSource() {
        return source;
    }

    public void setSource(Territory source) {
        this.source = source;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Default move constructor
     */
    public Move() {
        this.country = new Country();
        this.unit = new Unit();
        this.destination = new Territory();
        this.type = Type.D;
        this.status = Status.UNREAD;
        this.source = null;
    }

    /**
     * Most useful move constructor
     *
     * @param g     Game which is the state the move is using
     * @param id    int for the country making the move
     * @param orderOrig String[] for the order
     */
    public Move(Game g, int id, String[] orderOrig) {
        this.country = g.countries[id];
        boolean unitCheck = true;
        String[] order = new String[orderOrig.length-2];
        System.out.println(Arrays.toString(orderOrig));
        for(int i=0; i<order.length; i++)
            order[i]=orderOrig[i+2];
        for (Unit u : this.country.getUnits()) {
            if (order.length>2 && (u.isFleet() && order[0].equals("F") || (!u.isFleet() && order[0].equals("A")))
                    && (u.getLocation().equals(order[1]))) {
                if (u.hasOrder()) break;
                this.unit = u;
                unitCheck = false;
                this.unit.setHasOrder(true);
                break;
            }
        }
        if (unitCheck) {
            this.status = Status.FAILED;
            this.type = Type.D;
            this.destination = null;
            this.source = null;
            return;
        }
        //default is that the unit holds in place, we overwrite this if different
        this.type = Type.H;
        this.status = Status.PENDING;
        this.destination = this.unit.getLocation();
        //Hold
        if (order[2].equals("H"))
            return;
        //Support Hold
        if (order[2].equals("S") && order.length == 4) {
            if (unit.isFleet()) {
                for (Territory border : this.unit.getLocation().getNeighborsF()) {
                    if (border.equals(order[3]) && border.getOccupied() != -1) {
                        this.type = Type.SH;
                        this.status = Status.PENDING;
                        this.destination = border;
                        return;
                    }
                }
            } else {
                for (Territory border : this.unit.getLocation().getNeighborsA()) {
                    if (border.equals(order[3]) && border.getOccupied() != -1) {
                        this.type = Type.SH;
                        this.status = Status.PENDING;
                        this.destination = border;
                        return;
                    }
                }
            }
            //Not valid Support hold
            return;
        }
        //Support Attack
        if (order[2].equals("S") && order.length == 6) {
            if (!order[4].equals("-")) //improper syntax - resolves to hold on spot
                return;
            if (unit.isFleet()) {
                for (Territory border : this.unit.getLocation().getNeighborsF()) {
                    if (border.equals(order[5])) {
                        for (Territory border2 : border.allNeighbors()) {
                            if (border2.equals(order[3]) && border2.getOccupied() != -1) {
                                this.type = Type.SA;
                                this.status = Status.PENDING;
                                this.source = border;
                                this.destination = border2;
                                return;
                            }
                        }
                    }
                }
            } else {
                for (Territory border : this.unit.getLocation().getNeighborsA()) {
                    if (border.equals(order[5])) {
                        for (Territory border2 : border.allNeighbors()) {
                            if (border2.equals(order[3]) && border2.getOccupied() != -1) {
                                this.type = Type.SA;
                                this.status = Status.PENDING;
                                this.source = border;
                                this.destination = border2;
                                return;
                            }
                        }
                    }
                }
            }
            /*  this means that a potentially valid support attack could not be found thus
            this resolves to a hold on spot */
            return;
        }
        //Convoy
        if (order[2].equals("C")) {
            if (order.length != 6 || !order[4].equals("-")) //improper syntax - resolves to hold on spot
                return;
            if (!this.unit.isFleet()) //Armies cannot convoy - resolves to hold on spot
                return;
            else {
                for (Territory t : g.territories)
                    if (t.equals(order[5]))
                        for (Territory t2 : g.territories)
                            if (t2.equals(order[3])) {
                                if (t.equals(t2) || !t.coast() || !t2.coast()) return;
                                this.source = t2;
                                this.destination = t;
                                this.type = Type.C;
                                this.status = Status.PENDING;
                                return;
                            }
            }
            return;
        }
        //Move
        if (order[2].equals("-")) {
            if (unit.isFleet()) {
                for (Territory t : unit.getLocation().getNeighborsF())
                    if (t.equals(order[3])) {
                        this.destination = t;
                        this.type = Type.M;
                        this.status = Status.PENDING;
                        return;
                    }
            } else {
                //regular move
                for (Territory t : unit.getLocation().getNeighborsA())
                    if (t.equals(order[3])) {
                        this.destination = t;
                        this.type = Type.M;
                        this.status = Status.PENDING;
                        return;
                    }
                if (unit.getLocation().landlocked()) return;
                for (Territory t : g.territories)
                    if (t.equals(order[3]) && t.coast()) {
                        //potentially a convoy
                        this.destination = t;
                        this.status = Status.PENDING;
                        this.type = Type.CM;
                    }
            }
        }
    }

    /**
     * toString
     * @return obvious
     */
    public String toString(){
        if(this.status==Status.FAILED) return "Failed";
        String out = "";
        out += this.country.getId();
        out += " ";
        out += this.unit.getLocation().toString();
        out += " ";
        out += this.type;
        out += this.status;
        return out;
    }


    /**
     * Comparison for move with priority of status, type, destination, country, origin
     *
     * @param o other move to compare to
     * @return int for which is greater
     */
    @Override
    public int compareTo(Object o) {
        Move m = (Move) o;
        int scompare = this.status.compareTo(m.status);
        if (scompare != 0) return scompare;
        int tcompare = this.type.compareTo(m.type);
        if (tcompare != 0) return tcompare;
        int dcompare = this.destination.getName().compareTo(m.destination.getName());
        if (dcompare != 0) return dcompare;
        int ccompare = m.country.getId() - this.country.getId();
        if (ccompare != 0) return ccompare;
        return this.unit.getLocation().getName().compareTo(m.unit.getLocation().getName());
    }


    private static Comparator<Move> MoveCComparator
            = new Comparator<>() {
        /**
         * Compare moves m and n for Type Convoy.
         * @param m first move
         * @param n second move
         * @return 0 if neither or same, 1 if m is convoy, -1 if n is convoy
         */
        public int compare(Move m, Move n) {
            if(m.getType() ==  n.getType())
                return 0;
            else if(m.getType() == Type.C)
                return 1;
            else if(n.getType() == Type.C)
                return -1;
            else
                return 0;
        }

    };

    /**
     * sorts the array based on comparator for Type Convoy
     * inplace
     * @param moves arraylist to sort
     */
    public static void sortC(ArrayList<Move> moves){
        moves.sort(MoveCComparator);
    }
}



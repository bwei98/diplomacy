import com.sun.istack.internal.NotNull;

enum Type{
    D, H, S, C, M;
}
enum Status{
    UNREAD, EXECUTABLE, FAILED, PENDING;
}

public class Move {
    public Country country;
    public Unit unit;
    public Territory destination;
    public Type type;
    public Status status;

    /**
     * Basic constructor for a Move
     *
     * @param country     country making the order
     * @param unit        unit making the move
     * @param destination Territory representing the target
     * @param type        type of move made
     * @param status      status of the order
     */
    public Move(Country country, Unit unit, Territory destination, Type type, Status status) {
        this.country = country;
        this.unit = unit;
        this.destination = destination;
        this.type = type;
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
    }

    /**
     * Most useful move constructor
     * @param g Game which is the state the move is using
     * @param id int for the country making the move
     * @param order String[] for the order
     */
    public Move(@NotNull Game g, int id, String[] order) {
        this.country = g.countries[id];
        boolean unitCheck = true;
        for (Unit u : this.country.units) {
            if ((u.isFleet && order[0].equals("F") || (!u.isFleet && order[0].equals("A")))
                    && (u.location.equals(order[1]))) {
                if (u.hasOrder) {
                    this.status = Status.FAILED;
                    this.type = Type.D;
                    this.unit = null;
                    this.destination = null;
                    return;
                }
                this.unit = u;
                unitCheck = false;
                this.unit.hasOrder = true;
                break;
            }
        }
        if (unitCheck) {
            this.status = Status.FAILED;
            this.type = Type.D;
            this.destination = null;
            return;
        }
        //Hold
        if (order[2].equals("H")) {
            for (Territory t : g.territories) {
                if (t.name.equals(order[1])) {
                    this.type = Type.H;
                    this.status = Status.EXECUTABLE;
                    this.destination = this.unit.location;
                    return;
                }
            }
        }
        //Support Hold
        if (order[2].equals("S") && order.length == 4) {
            if (unit.isFleet) {
                for (Territory border : this.unit.location.neighborsF) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        this.type = Type.S;
                        this.status = Status.PENDING;
                        this.destination = border;
                        return;
                    }
                }
            } else {    //unit is army
                for (Territory border : this.unit.location.neighborsA) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        this.type = Type.S;
                        this.status = Status.PENDING;
                        this.destination = border;
                        return;
                    }
                }
            }
            //Not valid Support hold
            this.type = Type.S;
            this.status = Status.FAILED;
            this.destination = null;
            return;
        }

        //Support Attack
        if (order[2].equals("S") && order.length == 6) {
            if (!order[4].equals("-")) {
                this.type = Type.D;
                this.destination = null;
                this.status = Status.FAILED;
                return;
            }
            if (unit.isFleet) {
                for (Territory border : this.unit.location.neighborsF) {
                    if (border.equals(order[5])) {
                        for (Territory border2 : border.allNeighbors()) {
                            if (border2.equals(order[3]) && border2.occupied != -1) {
                                this.type = Type.S;
                                this.status = Status.PENDING;
                                this.destination = border2;
                                return;
                            }
                        }
                    }
                }
            } else {    //unit is army
                for (Territory border : this.unit.location.neighborsA) {
                    if (border.equals(order[5])) {
                        for (Territory border2 : border.allNeighbors()) {
                            if (border2.equals(order[3]) && border2.occupied != -1) {
                                this.type = Type.S;
                                this.status = Status.PENDING;
                                this.destination = border2;
                                return;
                            }
                        }
                    }
                }
            }
        }
        //TODO Convoy
        //TODO Move
    }
}


//
//        /**
//         * processOrder processes an order on a gamestate
//         * @param id int representing country giving order
//         * @param order String[] for the orders
//         * @return boolean on if the order was valid
//         */
//        private boolean processOrder(int id, String[] order){
//

/*
//Support Hold
        if(order[2].equals("S") && order.length==4){
            if(unit.isFleet) {
                for (Territory border : this.unit.location.neighborsF) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        border.takeStrength[border.occupied]++;
                        break;
                    }
                }
            } else {    //unit is army
                for (Territory border : this.unit.location.neighborsA) {
                    if (border.equals(order[3]) && border.occupied != -1) {
                        border.takeStrength[border.occupied]++;
                        break;
                    }
                }
            }
            //Not valid Support hold
            System.out.println("Not a valid support hold between " + order[1]+" and "+order[3] );
            return false;
 */



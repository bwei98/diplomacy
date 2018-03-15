import com.sun.istack.internal.NotNull                                                                                 ;

enum Type                                                                                                              {
    D, H, M, C, CM, SH, SA                                                                                             }
enum Status                                                                                                            {
    EXECUTED, EXECUTABLE, PENDING, UNREAD, FAILED                                                                      }

public class Move implements Comparable                                                                                {
    public Country country                                                                                             ;
    public Unit unit                                                                                                   ;
    public Territory destination                                                                                       ;
    public Territory source                                                                                            ;
    public Type type                                                                                                   ;
    public Status status                                                                                               ;

    /**
     * Basic constructor for a Move
     *
     * @param country     country making the order
     * @param unit        unit making the move
     * @param destination Territory representing the target
     * @param type        type of move made
     * @param status      status of the order
     * @param source      origin for two dest orders
     */
    public Move(Country country, Unit unit, Territory destination, Territory source, Type type, Status status)         {
        this.country = country                                                                                         ;
        this.unit = unit                                                                                               ;
        this.destination = destination                                                                                 ;
        this.type = type                                                                                               ;
        this.status = status                                                                                           ;
        this.source = source                                                                                          ;}

    /**
     * Default move constructor
     */
    public Move()                                                                                                      {
        this.country = new Country()                                                                                   ;
        this.unit = new Unit()                                                                                         ;
        this.destination = new Territory()                                                                             ;
        this.type = Type.D                                                                                             ;
        this.status = Status.UNREAD                                                                                    ;
        this.source = null                                                                                            ;}

    /**
     * Most useful move constructor
     * @param g Game which is the state the move is using
     * @param id int for the country making the move
     * @param order String[] for the order
     */
    public Move(@NotNull Game g, int id, String[] order)                                                               {
        this.country = g.countries[id]                                                                                 ;
        boolean unitCheck = true                                                                                       ;
        for (Unit u : this.country.units)                                                                              {
            if ((u.isFleet && order[0].equals("F") || (!u.isFleet && order[0].equals("A")))
                    && (u.location.equals(order[1])))                                                                  {
                if (u.hasOrder)                                                                                        {
                    this.status = Status.FAILED                                                                        ;
                    this.type = Type.D                                                                                 ;
                    this.unit = null                                                                                   ;
                    this.destination = null                                                                            ;
                    this.source = null                                                                                 ;
                    return                                                                                            ;}
                this.unit = u                                                                                          ;
                unitCheck = false                                                                                      ;
                this.unit.hasOrder = true                                                                              ;
                break                                                                                                ;}}
        if (unitCheck)                                                                                                 {
            this.status = Status.FAILED                                                                                ;
            this.type = Type.D                                                                                         ;
            this.destination = null                                                                                    ;
            this.source = null                                                                                         ;
            return                                                                                                    ;}
        //default is that the unit holds in place, we overwrite this if different
        this.type = Type.H                                                                                             ;
        this.status = Status.PENDING                                                                                   ;
        this.destination = this.unit.location                                                                          ;
        //Hold
        if (order[2].equals("H"))
            return                                                                                                     ;
        //Support Hold
        if (order[2].equals("S") && order.length == 4)                                                                 {
            if (unit.isFleet)                                                                                          {
                for (Territory border : this.unit.location.neighborsF)                                                 {
                    if (border.equals(order[3]) && border.occupied != -1)                                              {
                        this.type = Type.SH                                                                            ;
                        this.status = Status.PENDING                                                                   ;
                        this.destination = border                                                                      ;
                        return                                                                                      ;}}}
            else                                                                                                       {
                for (Territory border : this.unit.location.neighborsA)                                                 {
                    if (border.equals(order[3]) && border.occupied != -1)                                              {
                        this.type = Type.SH                                                                            ;
                        this.status = Status.PENDING                                                                   ;
                        this.destination = border                                                                      ;
                        return                                                                                      ;}}}
            //Not valid Support hold
            return                                                                                                    ;}
        //Support Attack
        if (order[2].equals("S") && order.length == 6)                                                                 {
            if (!order[4].equals("-")) //improper syntax - resolves to hold on spot
                return                                                                                                 ;
            if (unit.isFleet)                                                                                          {
                for (Territory border : this.unit.location.neighborsF)                                                 {
                    if (border.equals(order[5]))                                                                       {
                        for (Territory border2 : border.allNeighbors())                                                {
                            if (border2.equals(order[3]) && border2.occupied != -1)                                    {
                                this.type = Type.SA                                                                    ;
                                this.status = Status.PENDING                                                           ;
                                this.source = border                                                                   ;
                                this.destination = border2                                                             ;
                                return                                                                            ;}}}}}
            else                                                                                                       {
                for (Territory border : this.unit.location.neighborsA)                                                 {
                    if (border.equals(order[5]))                                                                       {
                        for (Territory border2 : border.allNeighbors())                                                {
                            if (border2.equals(order[3]) && border2.occupied != -1)                                    {
                                this.type = Type.SA                                                                    ;
                                this.status = Status.PENDING                                                           ;
                                this.source = border                                                                   ;
                                this.destination = border2                                                             ;
                                return                                                                            ;}}}}}
            /*  this means that a potentially valid support attack could not be found thus
            this resolves to a hold on spot */
            return                                                                                                    ;}
        //Convoy
        if(order[2].equals("C"))                                                                                       {
            if(order.length!=6 || !order[4].equals("-")) //improper syntax - resolves to hold on spot
                return                                                                                                 ;
            if(!this.unit.isFleet) //Armies cannot convoy - resolves to hold on spot
                return                                                                                                 ;
            else                                                                                                       {
                for(Territory t : g.territories)
                    if(t.equals(order[5]))
                       for(Territory t2 : g.territories)
                           if(t2.equals(order[3]))                                                                     {
                                if(t.equals(t2) || !t.coast() || !t2.coast()) return                                   ;
                                this.source=t2                                                                         ;
                                this.destination=t                                                                     ;
                                this.type=Type.C                                                                       ;
                                this.status=Status.PENDING                                                             ;
                                return                                                                               ;}}
            return                                                                                                    ;}
        //Move
        if(order[2].equals("M"))                                                                                       {
            if(unit.isFleet)                                                                                           {
                for(Territory t : unit.location.neighborsF)
                    if(t.equals(order[3]))                                                                             {
                        this.destination=t                                                                             ;
                        this.type=Type.M                                                                               ;
                        this.status=Status.PENDING                                                                     ;
                        return                                                                                       ;}}
            else                                                                                                       {
                //regular move
                for(Territory t : unit.location.neighborsA)
                    if(t.equals(order[3]))                                                                             {
                        this.destination=t                                                                             ;
                        this.type=Type.M                                                                               ;
                        this.status=Status.PENDING                                                                     ;
                        return                                                                                        ;}
                if(unit.location.landlocked()) return                                                                  ;
                for(Territory t : g.territories)
                    if(t.equals(order[4]) && t.coast())                                                                {
                        //potentially a convoy
                        this.destination = t                                                                           ;
                        this.status = Status.PENDING                                                                   ;
                        this.type = Type.CM                                                                        ;}}}}


    /**
     * Comparison for move with priority of status, type, destination, country, origin
     * @param o other move to compare to
     * @return int for which is greater
     */
    @Override
    public int compareTo(Object o)                                                                                     {
        Move m = (Move)o                                                                                               ;
        int scompare = this.status.compareTo(m.status)                                                                 ;
        if(scompare!=0) return scompare                                                                                ;
        int tcompare = this.type.compareTo(m.type)                                                                     ;
        if(tcompare!=0) return tcompare                                                                                ;
        int dcompare = this.destination.name.compareTo(m.destination.name)                                             ;
        if(dcompare!=0) return dcompare                                                                                ;
        int ccompare = m.country.id - this.country.id                                                                  ;
        if(ccompare!=0) return ccompare                                                                                ;
        return this.unit.location.name.compareTo(m.unit.location.name)                                               ;}}


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




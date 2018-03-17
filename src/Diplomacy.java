import java.util.Arrays;
import java.util.Scanner;

public class Diplomacy {
    public static int numCountries = 7; //TODO make this 7
    public static Country[] countries = new Country[numCountries];
    public static boolean won = false;
    public static Game gameState;
    public static int year;
    public static int season;

    /**
     * Initialize the game with the correct starts and units assigned
     */
    public static void init() {
        year = 1900;
        season = Map.SPRING;
        Map.initFull();

        /*------------------------------------------------*/
        Country England = new Country("England", new Territory[]{Map.Lvp, Map.Edi, Map.Lon}, 0);
        countries[0] = England;
        England.setUnits(new Unit[]{new Unit(England, true, Map.Lon), new Unit(England, false, Map.Lvp),
                                    new Unit(England, true, Map.Edi)});

        /*------------------------------------------------*/
        Country France = new Country("France", new Territory[]{Map.Par, Map.Bre, Map.Mar}, 1);
        countries[1] = France;
        France.setUnits(new Unit[]{ new Unit(France, true, Map.Bre), new Unit(France, false, Map.Par),
                                    new Unit(France, false, Map.Mar)});

        Country Germany = new Country("Germany",new Territory[]{Map.Kie, Map.Ber, Map.Mun},2);
        countries[2] = Germany;
        Germany.setUnits(new Unit[]{new Unit(Germany, true, Map.Kie), new Unit(Germany, false, Map.Ber),
                                    new Unit(Germany, false, Map.Mun)});

        Country Russia = new Country("Russia",new Territory[]{Map.Mos, Map.Stp, Map.Sev, Map.War},3);
        countries[3] = Russia;
        Russia.setUnits(new Unit[]{ new Unit(Russia, true, Map.Stp), new Unit(Russia, false, Map.Mos),
                                    new Unit(Russia, false, Map.War), new Unit(Russia, true, Map.Sev)});

        Country Austria = new Country("Austria",new Territory[]{Map.Vie, Map.Tri, Map.Bud},4);
        countries[4] = Austria;
        Austria.setUnits(new Unit[]{new Unit(Austria, true, Map.Tri), new Unit(Austria, false, Map.Vie),
                                    new Unit(Austria, false, Map.Bud)});

        Country Italy = new Country("Italy",new Territory[]{Map.Rom, Map.Ven, Map.Nap},5);
        countries[5] = Italy;
        Italy.setUnits(new Unit[]{  new Unit(Italy, true, Map.Nap), new Unit(Italy, false, Map.Ven),
                                    new Unit(Italy, false, Map.Rom)});

        Country Turkey = new Country("Turkey",new Territory[]{Map.Ank, Map.Con, Map.Smy},6);
        countries[6] = Turkey;
        Turkey.setUnits(new Unit[]{ new Unit(Turkey, true, Map.Ank), new Unit(Turkey, false, Map.Smy),
                                    new Unit(Turkey, false, Map.Con)});
        /*------------------------------------------------*/

        gameState = new Game(countries, Map.TERRITORIES, new Unit[0]);
    }

    /**
     * Run an iteration of the game
     * Possible move formats: A Lvn H; A Lvn - War; A Lvn S Ukr - War; A Lvn S War; F NTH C Nwy - Yor;
     * Example for current testing: A Lvp - Yor; F Lon - Wal; F Edi H
     * (and equivalent for fleets)
     */
    public static void runSeason() {
        Scanner reader = new Scanner(System.in);

        //Take orders for stage: progress
        String[][] moves = new String[numCountries][0];
        for(int i = 0; i < numCountries; i++) {
            if(countries[i].alive) {
                System.out.print(countries[i].name + " : what are your moves?\t");
                String moveLine = reader.nextLine();
                String[] cMoves = moveLine.split(";");

                for (int j = 0; j < cMoves.length; j++) cMoves[j] = i + " : " + cMoves[j].trim();
                moves[i] = cMoves;
            }
        }

        System.out.println("Got it");

        gameState = gameState.movephase(moves);
        Unit[] retreats = gameState.retreatingUnits;

        System.out.println("Retreats: " + (retreats.length > 0));

        //Take orders for stage: retreat if necessary
        if(retreats.length > 0) {
            String[] destinations = new String[retreats.length];

            Arrays.sort(retreats);
            int prevCountry = -1;
            for (int i = 0; i < retreats.length; i++) {
                Unit retreat = retreats[i];
                if(retreat.owner.id != prevCountry) {
                    prevCountry = retreat.owner.id;
                    System.out.println(countries[retreat.owner.id].name + " : please enter your retreats ");
                }
                System.out.println(retreat + ": ");
                System.out.print("Options - DISBAND");
                for(Territory t : retreat.canMove()) System.out.print(", " + t);
                System.out.print("\n Where do you want to move this unit? \t");

                String order = reader.nextLine();
                destinations[i] = order.trim();
            }

            gameState = gameState.retreatphase(destinations);
        }
    }

    /**
     * Asks the users who require builds or disbands and processes their inputs
     * Desired format: A Smy; F ADR
     */
    public static void runBuild() {
        Scanner reader = new Scanner(System.in);

        String[][] buildMoves = new String[numCountries][0];
        for(int i = 0; i < numCountries; i++) {
            Country country = countries[i];
            if(country.alive) {
                System.out.println(country.name + ": ");
                int diff = country.numBuildsOrDisbands();
                //Either disband or build
                if (diff > 0) {
                    System.out.println("You have to disband " + diff + " armies.");
                    System.out.println("You may choose from: ");
                    for (Unit unit : country.units) System.out.println(unit);
                    System.out.print("Enter your semicolon-separated disbands (specify F/A): \t");
                    buildMoves[i] = reader.nextLine().split("; ");
                } else if (diff < 0) {
                    System.out.println("You may build " + (-diff) + " armies.");
                    System.out.print("You may build in: ");
                    for(Territory sc : country.homeSCs) {
                        if (country.canBuild(sc)) {
                            System.out.print(sc.name + " ");
                            if (sc.coast()) System.out.print("(F/A) ");
                            if (sc.water()) System.out.print("(F)");
                            if (sc.landlocked()) System.out.print("(A) ");
                        }
                    }
                    System.out.print("\n Enter your semicolon-separated builds (specify F/A): \t");
                    buildMoves[i] = reader.nextLine().split("; ");
                }
            }
        }
        gameState = gameState.buildphase(buildMoves);
    }

    /**
     * Give countries supply centers and take them away as needed
     */
    public static void resolveUnits() {
        for(Country country : countries) {
            for(Unit unit : country.units) {
                if(unit.location.isSupplyCenter() && unit.location.supplyCenter != unit.owner.id) {
                    country.gainSupplyCenter(unit.location);
                    if(unit.location.supplyCenter != -1)
                        countries[unit.location.supplyCenter].loseSupplyCenter(unit.location);
                    unit.location.supplyCenter = unit.owner.id;
                }
            }
        }
    }

    /**
     * Hi I'm the main function what's up
     * @param args - Hi we're the command line arguments we do nothing
     */
    public static void main(String[] args) {
        init();
        while(!won) {
            String seas = "Fall  "; if(season == Map.SPRING) seas = "Spring"; if(season == Map.WINTER) seas = "Winter";
            String message =  "==========================\n";
            message += "==       Year: " + year + "     ==\n";
            message += "==     Season: " + seas + "   ==\n";
            message += "==========================";
            System.out.println(message);

            for(int i = 0; i < numCountries; i++) System.out.println(countries[i].toString());

            if(season == Map.SPRING) {
                runSeason();
                season = (season + 1) % 3;
            } else if (season == Map.FALL) {
                runSeason();
                resolveUnits();
                season = (season + 1) % 3;
            } else if(season == Map.WINTER) {
                runBuild();
                season = Map.SPRING;
                year++;
            }
        }
    }

}

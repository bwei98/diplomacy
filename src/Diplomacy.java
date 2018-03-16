import java.util.Arrays;
import java.util.Scanner;

public class Diplomacy {
    public static int numCountries = 1; //TODO make this 7
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
        Map.initMap();

        gameState = new Game(countries, Map.TERRITORIES, new Unit[0]);
    }

    /**
     * Run an iteration of the game
     * Possible move formats: A Lvn H; A Lvn - War; A Lvn S Ukr - War; A Lvn S War; F NTH C Nwy - Yor;
     * (and equivalent for fleets)
     */
    public static void runSeason() {
        Scanner reader = new Scanner(System.in);

        //Take orders for stage: progress
        String[][] moves = new String[numCountries][0];
        for(int i = 0; i < numCountries; i++) {
            if(countries[i].alive) {
                System.out.println(countries[i].name + " : what are your moves? ");
                String moveLine = reader.nextLine();
                String[] cMoves = moveLine.split(";");
                for (int j = 0; j < cMoves.length; i++) cMoves[j] = i + " : " + cMoves[j].trim();
                moves[i] = cMoves;
            }
        }
        gameState = gameState.movephase(moves);
        Unit[] retreats = gameState.retreatingUnits;

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
                System.out.println("\n Where do you want to move this unit?");

                String order = reader.nextLine();
                destinations[i] = order.trim();
            }

            gameState = gameState.retreatphase(destinations);
        }

        String seas = "Fall  "; if(season == Map.SPRING) seas = "Spring "; if(season == Map.WINTER) seas = "Winter";
        String message =  "==========================\n";
        message += "==        Year: " + year + "    ==\n";
        message += "==      Season: " + seas + "  ==\n";
        message += "==========================";
        System.out.println(message);

        for(int i = 0; i < numCountries; i++) System.out.println(countries[i].toString());
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
                if (diff < 0) {
                    System.out.println("You have to disband " + diff + " armies.");
                    System.out.println("You may choose from: ");
                    for (Unit unit : country.units) System.out.println(unit);
                    System.out.println("Enter your colon-separated disbands (specify F/A)");
                    buildMoves[i] = reader.nextLine().split("; ");
                } else if (diff > 0) {
                    System.out.println("You may build " + diff + " armies.");
                    System.out.println("You may build in: ");
                    for(Territory sc : country.homeSCs) if(country.canBuild(sc)) System.out.println(sc.name);
                    System.out.println("Enter your colon-separated builds (specific F/A)");
                    buildMoves[i] = reader.nextLine().split("; ");
                }
            }
        }
        gameState = gameState.buildphase(buildMoves);
    }

    /**
     * Hi I'm the main function what's up
     * @param args - Hi we're the command line arguments we do nothing
     */
    public static void main(String[] args) {
        init();
        while(!won) {
            if(season == Map.SPRING || season == Map.FALL) {
                runSeason();
                season = (season + 1) % 3;
            }
            if(season == Map.SPRING) year++;
            if(season == Map.WINTER) {
                runBuild();
                season = Map.SPRING;
            }
        }
    }

}

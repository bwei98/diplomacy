import java.util.*;
public class Diplomacy {
    public static int numCountries = 7;
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
        gameState = new Game(countries, Map.TERRITORIES, false);
    }

    /**
     * Run an iteration of the game
     * Possible move formats: A Lvn H; A Lvn - War; A Lvn S Ukr - War; A Lvn S War; F NTH C Nwy - Yor;
     * (and equivalent for fleets)
     */
    public static void runSeason() {
        Scanner reader = new Scanner(System.in);

        //Take orders for stage: progress
        String[] move;
        for(int i = 0; i < numCountries; i++) {
            System.out.println(countries[i].name + " : what are your moves? ");
            String moveLine = reader.nextLine();
            String[] moves = moveLine.split(";");
            for(int j = 0; j < moves.length; i++) moves[j] = i + " : " + moves[j].trim();
        }
//        move is not initialized...
//        gameState = gameState.movephase(move);
        boolean retreat = gameState.doIneedafuckingretreat;

        //Take orders for strage: retreat if necessary
        if(retreat) {
            for(int i = 0; i < numCountries; i++) {
                //TODO
            }
        }

        //TODO print the current state
        String seas = "Fall  "; if(season == Map.SPRING) seas = "Spring "; if(season == Map.WINTER) seas = "Winter";
        String message =  "==========================\n";
        message += "==        Year: " + year + "    ==\n";
        message += "==      Season: " + seas + "  ==\n";
        message += "==========================";
        System.out.println(message);

        for(int i = 0; i < numCountries; i++) System.out.println(countries[i].toString());
    }

    public static void runBuild() {
        Scanner reader = new Scanner(System.in);

        //TODO Take orders for stage: build
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

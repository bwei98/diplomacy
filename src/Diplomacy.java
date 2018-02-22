public class Diplomacy {
    public static int numCountries = 7;
    public static Country[] countries = new Country[numCountries];

    /**
     * Initialize the game with the correct starts and units assigned
     */
    public static void init() {
    }

    /**
     * Run an iteration of the game
     * Possible move formats: A Lvn H; A Lvn - War; A Lvn S Ukr - War; A Lvn S War; F NTH C Nwy - Yor;
     * (and equivalent for fleets)
     */
    public static void run() {
        Scanner reader = new Scanner(System.in);
        for(int i = 0; i < numCountries; i++) {
            System.out.println(countries[i].name + " : what are your moves? ");
            String moveLine = reader.nextLine();
            String[] moves = moveLine.split(";");
        }
    }
    public static void main(String[] args) {

    }


}

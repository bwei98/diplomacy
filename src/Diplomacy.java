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
     */
    public static void run() {
        Scanner reader = new Scanner(System.in);
        for(int i = 0; i < numCountries; i++) {
            System.out.println(countries[i].name + " : what are your moves? ");
            String moves = reader.nextLine();
        }
    }
    public static void main(String[] args) {

    }


}

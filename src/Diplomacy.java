import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Diplomacy {
    public static int numCountries = 3;//7;
    public static Country[] countries = new Country[numCountries];
    public static boolean won = false;
    public static Game gameState;
    public static int year;
    public static int season;
    public static Scanner inputReader;
    public static Scanner checkReader;
    public static boolean verbose;

    /**
     * Initialize the game with the correct starts and units assigned
     */
    public static void init(String trace) {
        if(trace.equals("System.in")) {
            inputReader = new Scanner(System.in);
            checkReader = new Scanner(System.in);
        } else {
            try {
                inputReader = new Scanner(new File("traces/" + trace));
                checkReader = new Scanner(new File("traces/expected/" + trace));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        year = 1901;
        season = Map.SPRING;
        //Map.initFull();
        Map.initUK_FR_GR();

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
        /*------------------------------------------------
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
        ------------------------------------------------*/
        gameState = new Game(countries, Map.TERRITORIES, new Unit[0]);
    }

    /**
     * Run an iteration of the game
     * Possible move formats: A Lvn H; A Lvn - War; A Lvn S Ukr - War; A Lvn S War; F NTH C Nwy - Yor;
     * Example for current testing: A Lvp - Yor; F Lon - Wal; F Edi H
     * (and equivalent for fleets)
     */
    public static void runSeason() {

        //Take orders for stage: progress
        String[][] moves = new String[numCountries][0];
        for(int i = 0; i < numCountries; i++) {
            if(countries[i].isAlive()) {
                if(verbose) System.out.print(countries[i].getName() + " : what are your moves?\t");
                String s = readNextLine(inputReader);
                String[] cMoves = processInput(s).split("; ");

                for (int j = 0; j < cMoves.length; j++) cMoves[j] = i + " : " + cMoves[j].trim();
                moves[i] = cMoves;
            }
        }

   //     System.out.println("Got it");

        gameState = gameState.movephase(moves);
        Unit[] retreats = gameState.retreatingUnits;

//        System.out.println("Retreats: " + (retreats.length > 0));

        //Take orders for stage: retreat if necessary
        if(retreats.length > 0) {
            String[] destinations = new String[retreats.length];

            Arrays.sort(retreats);
            int prevCountry = -1;
            for (int i = 0; i < retreats.length; i++) {
                Unit retreat = retreats[i];
                if(retreat.getOwner().getId() != prevCountry) {
                    prevCountry = retreat.getOwner().getId();
                    System.out.println(countries[retreat.getOwner().getId()].getName() + " : please enter your retreats ");
                }
                System.out.println(retreat + ": ");
                System.out.print("Options - DISBAND");
                for(Territory t : retreat.canMove()) System.out.print(", " + t);
                System.out.print("\n Where do you want to move this unit? \t");

                String s = readNextLine(inputReader);
                String order = s.toUpperCase();
                System.out.println(order);
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

        String[][] buildMoves = new String[numCountries][0];
        for(int i = 0; i < numCountries; i++) {
            Country country = countries[i];
            if(country.isAlive()) {
                System.out.println(country.getName() + ": ");
                int diff = country.numBuildsOrDisbands();
                //System.out.println(diff);
                //Either disband or build
                if (diff < 0) {
                    System.out.println("You have to disband " + (-diff) + " armies.");
                    System.out.println("You may choose from: ");
                    for (Unit unit : country.getUnits()) System.out.println(unit);
                    System.out.print("Enter your semicolon-separated disbands (specify F/A): \t");
                    String s = readNextLine(inputReader);
                    buildMoves[i] = processInput(s).split("; ");
                } else if (diff > 0) {
                    System.out.println("You may build " + diff + " armies.");
                    System.out.print("You may build in: ");
                    for(Territory sc : country.getHomeSCs()) {
                        if (country.canBuild(sc)) {
                            System.out.print(sc.getName() + " ");
                            if (sc.coast()) System.out.print("(F/A) ");
                            if (sc.water()) System.out.print("(F)");
                            if (sc.landlocked()) System.out.print("(A) ");
                        }
                    }
                    System.out.print("\n Enter your semicolon-separated builds (specify F/A): \t");
                    String s = readNextLine(inputReader);
                    buildMoves[i] = processInput(s).split("; ");
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
            for(Unit unit : country.getUnits()) {
                if(unit.getLocation().isSupplyCenter() && unit.getLocation().getSupplyCenter() != unit.getOwner().getId()) {
                    if(unit.getLocation().getSupplyCenter() != -1)
                        countries[unit.getLocation().getSupplyCenter()].loseSupplyCenter(unit.getLocation());
                    country.gainSupplyCenter(unit.getLocation());
                    unit.getLocation().setSupplyCenter(unit.getOwner().getId());
                }
            }
        }
    }


    public static boolean checkState(){
        String line;
        if(verbose) System.out.println("EXPECTED: ");
        for(int i = 0; i < numCountries; i++){
            if((line = readNextLine(checkReader)).equals("pass")) return true;
            Country c = gameState.countries[i];
            Unit[] units = c.getUnits();
            String[] expectedUnits = line.split(";");
            if(expectedUnits.length != units.length) return false;
            for(String unit : expectedUnits){
                unit = unit.trim();
                boolean type = unit.startsWith("F"); // true == F; false == A
                String location = unit.substring(2);
                boolean ind = false;
                for(Unit u : units){
                    if(u.isFleet() == type && u.getLocation().toString().equals(location)) {
                        ind = true;
                        break;
                    }
                }
                if(!ind) {
                    System.out.println("Unit locations incorrect for " + gameState.countries[i].getName());
                    return false;
                }
            }
            if((line = readNextLine(checkReader)).equals("pass")) return true;
            String[] expectedSc = line.split(";");
            Territory[] SCs = c.getSupplyCenters();
            if(expectedSc.length != SCs.length) return false;
            for(String sc : expectedSc){
                sc = sc.trim();
                boolean ind = false;
                for(Territory t : SCs){
                    if(t.getName().equals(sc)) {
                        ind = true;
                        break;
                    }
                }
                if(!ind) {
                    System.out.println("Supply Centers incorrect for " + gameState.countries[i].getName());
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Hi I'm the main function what's up
     * @param args - Hi we're the command line arguments we do nothing
     */
    public static void main(String[] args) {

        Options options = new Options();

        Option trace = new Option("t", "trace", true, "trace file");
        trace.setRequired(false);
        options.addOption(trace);

        Option output = new Option("v", "verbose", false, "verbosity");
        output.setRequired(false);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Diplomacy", options);
            return;
        }

        boolean traceMode = false;
        String traceName = "System.in";
        verbose = cmd.hasOption("v");
        if(cmd.hasOption("t")) {
            traceName = cmd.getOptionValue("t");
            traceMode = true;
        } else {
            verbose = true;
        }

        init(traceName);

        while(!won) {
            String seas = "Fall  "; if(season == Map.SPRING) seas = "Spring"; if(season == Map.WINTER) seas = "Winter";
            String message =  "==========================\n";
            message += "==       Year: " + year + "     ==\n";
            message += "==     Season: " + seas + "   ==\n";
            message += "==========================";
            if(verbose) {
                System.out.println(message);
                for (int i = 0; i < numCountries; i++)
                    System.out.print(countries[i].toString());
            }
            try {
                switch (season) {
                    case Map.SPRING:
                        runSeason();
                        season = (season + 1) % 3;
                        break;
                    case Map.FALL:
                        runSeason();
                        resolveUnits();
                        season = (season + 1) % 3;
                        break;
                    case Map.WINTER:
                        runBuild();
                        season = Map.SPRING;
                        year++;
                        break;
                }
                if (traceMode && !checkState()) {
                    System.out.println("Tests failed for "
                            + (season == Map.WINTER ? "Fall " : (season == Map.FALL ? "Spring " : "Winter "))
                            + (season == Map.SPRING ? (year - 1) : year));
                    return;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Reached end of trace without error");
                return;
            }
        }
    }

    /**
     * Removes semicolons and standardizes formatting
     * @param o - the input to process
     * @return - the formatted input
     */
    public static String processInput(String o) {
        o = o.trim().toUpperCase();
        if(o.endsWith(";")) o = o.substring(0, o.length() - 1);
        return o;
    }


    private static String readNextLine(Scanner s){
        String line;
        while((line = s.nextLine()).startsWith("#")) {}
        if(verbose) System.out.println(line);
        return line;
    }

}

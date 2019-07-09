# diplomacy

### Build and run instructions

Java 10.0 or higher might be required

To run from IDE: Run -> Edit Configurations.  Set main class to `Diplomacy`, enter cmdline args to Program Arguments, if desired

To run from command line:
 
Compile with:
`javac -cp lib/*:. src/*.java -d bin`

Then run with:
`java -cp lib/*:./bin Diplomacy <args>`

#### Arguments
    java -cp lib/*:./bin Diplomacy [-v] [-t trace]

    -t, --trace <tracefile>: name trace to execute
    -v, --verbose: print moves, requests, and gamestate in each iteration; default is off; forced on if no trace is provided

### Using a subset of the current available map (full and UK-only are the current coded options)

To change the map
    In Diplomacy.java:
        Change numCountries
        Call the approriate initialization method in init (create one if necessary)
        Comment out the unused countries
    In Map.java:
        Change NUM_TERRITORIES

### Known bugs:

Moving into a friendly territory that is evacuated in the same turn (e.g. A Ber - Kie, F Kie - Den) fails
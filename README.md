# diplomacy

### Build and run instructions

Java 10.0 or higher might be required

To run from IDE: Run -> Edit Configurations.  Set main class to `Diplomacy`, enter cmdline args to Program Arguments, if desired

To run from command line:
 
Compile with:
`javac -cp lib/*:. src/*.java -d bin`

Then run with:
`java -cp lib/*:./bin Diplomacy <args>

#### Arguments
    java -cp lib/*:./bin Diplomacy [-v] [-t trace]

    -t, --trace <tracefile>: name trace to execute
    -v, --verbose: print moves, requests, and gamestate in each iteration; default is off; forced on if no trace is provided


### Known bugs:
Traces and verbosity has NOT been implemented with builds and retreats

Moving into a friendly territory that is evacuated in the same turn (e.g. A Ber - Kie, F Kie - Den) fails

Builds are not given for newly acquired supply centers
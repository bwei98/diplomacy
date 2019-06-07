# diplomacy

**Build and run instructions**

Java 10.0 or higher might be required

To run from IDE: run `Diplomacy`

To run from command line:
 
Compile with:
`javac -cp lib/*:. src/*.java -d bin`

Then run with:
`java -cp ./bin Diplomacy`


**Known bugs:**

Moving into a friendly territory that is evacuated in the same turn (e.g. A Ber - Kie, F Kie - Den) fails

Builds are not given for newly acquired supply centers
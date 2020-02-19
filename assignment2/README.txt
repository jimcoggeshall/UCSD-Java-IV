To compile:
javac -cp lib/junit-4.12.jar -d . src/*/java/edu/ucsd/javaiv/assignment2/*.java

To run unit tests:
java -Xmx9g -Xms9g -cp .:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucsd.javaiv.assignment2.TestBinarySearch

To run (for example):
java edu.ucsd.javaiv.assignment2.BinarySearch 34

To generate javadoc:
javadoc -cp .:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar -d doc/ src/*/java/edu/ucsd/javaiv/assignment2/*.java

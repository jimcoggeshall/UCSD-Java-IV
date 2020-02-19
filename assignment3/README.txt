***
* Changes are marked with "// jcc":
***
    [jcc@Jamess-Air Assignment3]$ ack '// jcc' src/
    src/main/java/com/act365/sudoku/Composer.java
    41:    private List<Grid> puzzles;  // jcc - changed from Vector to interface List<Grid>, and added private to ensure thread safety
    186:        puzzles = Collections.synchronizedList(new ArrayList<>());  // jcc - changed from Vector to synchronized ArrayList
    443:            puzzles.add(puzzle);  // jcc - changed from addElement to add
    608:                    gridContainer.setGrid(puzzles.get(0));  // jcc - replace elementAt with get and removed cast to Grid
    622:                        System.out.println((puzzles.get(mostComplex)).toString());   // jcc - replace elementAt with get and removed cast to Grid

    src/main/java/com/act365/sudoku/StateStack.java
    27:import java.util.ArrayList;  // jcc - replaced import for Vector with ArrayList
    33: * this implementation extends ArrayList and hence is not thread-safe. // jc
    35:public class StateStack extends ArrayList<Object>  // jcc - replaced parent class Vector with ArrayList<Object>
    46:     * to hold // jcc
    47:     * @throws java.util.ConcurrentModificationException // jcc
    53:        ensureCapacity(maxMoves);  // jcc - replaced setSize with ensureCapacity
    58:     * corresponding to move number. // jcc
    60:     * @param obj the state object to add to this StateStack // jcc
    62:     * append) the state object // jcc
    63:     * @throws java.util.ConcurrentModificationException // jcc
    75:            set(i, obj);  // jcc - replaced setElementAt with set
    78:            add(obj);  // jcc - replaced addElement with add
    85:     * corresponding to move number. // jcc
    88:     * object (null if not present) // jcc
    89:     * @return the state object at the specified position // jcc
    90:     * @throws java.util.ConcurrentModificationException // jcc
    102:            return get(i);  // jcc - replaced elementAt with get
    [jcc@Jamess-Air Assignment3]$ 

***
* To compile:
***
    javac -d . src/main/java/com/act365/{awt/,sudoku/{,masks/}}*.java

***
* To generate javadoc:
***
    javadoc -d doc src/main/java/com/act365/{awt/,sudoku/{,masks/}}*.java

***
* To run:
***
    java -cp . com.act365.sudoku.SuDoku

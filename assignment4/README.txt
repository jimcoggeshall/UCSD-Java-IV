***
* Changes are marked with "// jcc":
***
    [jcc@Jamess-Air Assignment4]$ ack -c '// jcc'  src | awk -F: '$NF > 0'
    src/main/java/com/act365/sudoku/GridContainer.java:71
    src/main/java/com/act365/sudoku/SuDoku.java:3
    [jcc@Jamess-Air Assignment4]$ 

***
* A few example saved games are in share/:
***
    [jcc@Jamess-Air Assignment4]$ ls share/
    empty_game.dat      in_progress_game.dat    solved_game.dat
    [jcc@Jamess-Air Assignment4]$

***
* To compile:
***
    javac -d . src/main/java/com/act365/{awt/,sudoku/{,masks/}}*.java

***
* To run:
***
    java -cp . com.act365.sudoku.SuDoku

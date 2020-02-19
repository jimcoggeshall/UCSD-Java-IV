package edu.ucsd.javaiv.finalproject.clueandanswer;

import edu.ucsd.javaiv.finalproject.CircularJokeStrategy;
import edu.ucsd.javaiv.finalproject.Joke;
import edu.ucsd.javaiv.finalproject.JokeFormatException;
import edu.ucsd.javaiv.finalproject.JokeHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link edu.ucsd.javaiv.finalproject.JokeHandler} specifically for 
 * {@link edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJoke} jokes. 
 * <p>
 * In particular, the jokes file used must contain two fields (a question and an answer) per line.
 * 
 * @author jcc
 */
public class ClueAndAnswerJokeHandler extends JokeHandler {

    /**
     * Validation function for {@link edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJoke}s.
     * @param joke The joke to validate. 
     * @throws JokeFormatException If the joke is invalid.
     */
    public void validateClueAndAnswerJoke(ClueAndAnswerJoke joke) throws JokeFormatException {
        validateJoke(joke);
    }
    
    /**
     * Constructor.
     */
    public ClueAndAnswerJokeHandler() {}

    /**
     * Read jokes from the specified file, validate them one-at-a-time, and initialize the joke-
     * telling strategy. 
     * 
     * @param inputFilename Path to tab-delimited input file containing jokes.
     * @throws IOException If the specified file cannot be read.
     * @throws JokeFormatException If the file is formatted incorrectly. 
     */
    public void init(String inputFilename) throws IOException, JokeFormatException {
        BufferedReader br = new BufferedReader(new FileReader(inputFilename));
        List<Joke> jokeList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split("\t");
            if (lineArr.length != 2) {
                throw new JokeFormatException("Format error: " + line);
            }
            String clue = lineArr[0];
            String answer = lineArr[1];
            Joke theJoke = new ClueAndAnswerJoke(clue, answer);
            validateClueAndAnswerJoke((ClueAndAnswerJoke) theJoke);
            jokeList.add(theJoke);
        }
        setJokeStrategy(new CircularJokeStrategy(jokeList));
    }

    /**
     * Get the clue corresponding to the current joke. 
     * @return The clue. 
     */
    public String getClue() {
        return ((ClueAndAnswerJoke) getJokeStrategy().getCurrentJoke()).getClue();
    }

    /**
     * Get the answer corresponding to the current joke.
     * @return The answer. 
     */
    public String getAnswer() {
        return ((ClueAndAnswerJoke) getJokeStrategy().getCurrentJoke()).getAnswer();
    }

}

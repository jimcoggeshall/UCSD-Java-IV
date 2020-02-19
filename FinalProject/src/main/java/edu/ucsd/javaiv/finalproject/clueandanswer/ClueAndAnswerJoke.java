package edu.ucsd.javaiv.finalproject.clueandanswer;

import edu.ucsd.javaiv.finalproject.Joke;

/**
 * Simple class for establishing and implementing the operations that must be supported by a "clue-
 * and-answer joke".
 * 
 * @author jcc
 */
public class ClueAndAnswerJoke implements Joke {

    private final String clue;
    private final String answer;

    /**
     * Constructor.
     * 
     * @param clue This joke's clue.
     * @param answer This joke's answer.
     */
    public ClueAndAnswerJoke(String clue, String answer) {
        this.clue = clue;
        this.answer = answer;
    }

    /**
     * Get this joke's clue,
     * 
     * @return The clue.
     */
    public String getClue() {
        return this.clue;
    }

    /**
     * Get this joke's answer.
     * 
     * @return The answer.
     */
    public String getAnswer() {
        return this.answer;
    }

}

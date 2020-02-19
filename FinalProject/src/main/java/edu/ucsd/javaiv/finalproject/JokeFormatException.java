package edu.ucsd.javaiv.finalproject;

/**
 * Exception specifically for errors due to badly-formatted jokes.
 * 
 * @author jcc
 */
public class JokeFormatException extends Exception {

    private static final long serialVersionUID = -5382502543404527981L;

    /**
     * Constructor.
     * @param message A descriptive message to propagate along with the exception.
     */
    public JokeFormatException(String message) {
        super(message);
    }
    
}

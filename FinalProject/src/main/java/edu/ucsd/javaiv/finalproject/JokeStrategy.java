package edu.ucsd.javaiv.finalproject;

/**
 * Interface to an object encapsulating all intelligence regarding how the a list of jokes is to be
 * traversed, and keeping track of the "current" joke. 
 * 
 * @author jcc
 */
public interface JokeStrategy {
    
    /**
     * Get the current joke. 
     * @return The current joke.
     */
    public Joke getCurrentJoke();
    
    /**
     * Choose the "next" joke according to the strategy and make it the new "current" joke.
     * @return The new joke. 
     */
    public Joke getNextJoke();  
    
}

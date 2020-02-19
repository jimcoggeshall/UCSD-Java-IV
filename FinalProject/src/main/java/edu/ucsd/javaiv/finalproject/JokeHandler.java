package edu.ucsd.javaiv.finalproject;

/**
 * Class responsible for maintaining all intelligence regarding the content of the jokes. 
 * <p>
 * An instance of this class is contained by the {@link edu.ucsd.javaiv.finalproject.JokeProtocol}
 * object used by a server thread. Its responsibilities include holding both the list of jokes and 
 * the strategy used to cycle through them, and validating the format of each of the jokes on the 
 * list.
 * 
 * @author jcc
 */
public abstract class JokeHandler {
    
    private JokeStrategy jokeStrategy;
    
    /**
     * Validate the format of this joke.
     * 
     * @param joke Joke to be validated. 
     * @throws JokeFormatException If the joke format is invalid.
     */
    public void validateJoke(Joke joke) throws JokeFormatException {};
    
    /**
     * Return the {@link edu.ucsd.javaiv.finalproject.JokeStrategy}.
     * @return The joke iteration strategy.
     */
    public JokeStrategy getJokeStrategy() {
        return jokeStrategy;
    }
    
    /**
     * Set the {@link edu.ucsd.javaiv.finalproject.JokeStrategy}.
     * @param jokeStrategy The joke iteration strategy.
     */
    public void setJokeStrategy(JokeStrategy jokeStrategy) {
        this.jokeStrategy = jokeStrategy;
    }
    
    /**
     * Get a new joke, using the contained {@link edu.ucsd.javaiv.finalproject.JokeStrategy}.
     */
    public void retrieveNewJoke() {
        getJokeStrategy().getNextJoke();
    }
    
}

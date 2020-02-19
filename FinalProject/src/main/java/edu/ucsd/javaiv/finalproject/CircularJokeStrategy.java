package edu.ucsd.javaiv.finalproject;

import edu.ucsd.javaiv.finalproject.Joke;
import edu.ucsd.javaiv.finalproject.JokeStrategy;
import java.util.List;
import java.util.Random;

/**
 * Class implementing all operations necessary to maintain and traverse the joke list in a 
 * "circular" manner.
 * <p>
 * Specifically, this class implements a joke-telling strategy in which the initial joke is chosen
 * at random and subsequent jokes are retrieved sequentially according to their order on the list.
 * When the last element of the list is reached, iteration starts again at the top of the list. 
 *
 * @author jcc
 */
public class CircularJokeStrategy implements JokeStrategy {

    private final List<Joke> jokes;
    private int currentJoke;
    
    private Random random;
    
    private int getRandomJokeIndex() {
        return random.nextInt(jokes.size() - 1);
    }
    
    /**
     * Construct a new object using the given list of jokes, and sets the current joke to a 
     * random element of the list.
     * 
     * @param jokes The list of jokes.
     */
    public CircularJokeStrategy(List<Joke> jokes) {
        this.random = new Random();
        this.jokes = jokes;
        this.currentJoke = getRandomJokeIndex();
    }

    /**
     * Get the current joke.
     * @return The current joke.
     */
    @Override
    public Joke getCurrentJoke() {
        return jokes.get(currentJoke);
    }

    /**
     * Choose the next joke sequentially and make it the new "current" joke. 
     * <p>
     * On reaching the end of the list, the next joke is set to the first element of the list.
     * 
     * @return The new joke. 
     */
    @Override
    public Joke getNextJoke() {
        currentJoke++;
        if (currentJoke > jokes.size()) {
            currentJoke = 0;
        }
        return getCurrentJoke();
    }

}

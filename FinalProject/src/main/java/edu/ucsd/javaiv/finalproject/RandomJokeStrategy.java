package edu.ucsd.javaiv.finalproject;

import java.util.List;
import java.util.Random;

/**
 * Class implementing all operations necessary to maintain and traverse the joke list in a 
 * random manner.
 *
 * @author jcc
 */
public class RandomJokeStrategy implements JokeStrategy {

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
    public RandomJokeStrategy(List<Joke> jokes) {
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
     * Choose the next joke at random and make it the new "current" joke. 
     * 
     * @return The new joke. 
     */
    @Override
    public Joke getNextJoke() {
        currentJoke = getRandomJokeIndex();
        return getCurrentJoke();
    }

}
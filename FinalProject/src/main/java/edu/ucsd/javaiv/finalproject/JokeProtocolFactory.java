package edu.ucsd.javaiv.finalproject;

import java.io.IOException;

/**
 * Abstract class responsible for producing {@link edu.ucsd.javaiv.finalproject.JokeProtocol}
 * objects. 
 * <p>
 * The server contains an instance of a concrete implementation of this factory, which 
 * is configured via GUI input, then uses the factory to create new protocol objects necessary 
 * for client communication. 
 * 
 * @see edu.ucsd.javaiv.finalproject.knockknock.KnockKnockProtocolFactory
 * @author jcc
 */
public abstract class JokeProtocolFactory {
    
    /**
     * Set the input file from which to read jokes. 

     * @param inputFilename Path to input file.
     * @throws IOException If the file cannot be read.
     * @throws JokeFormatException If the file is formatted incorrectly. 
     */
    public abstract void setJokesFile(String inputFilename) throws IOException, JokeFormatException;
    
    /**
     * Create a new  {@link edu.ucsd.javaiv.finalproject.JokeProtocol} object, 
     * configured to use jokes in the file specified in 
     * {@link edu.ucsd.javaiv.finalproject.JokeProtocolFactory#setJokesFile(String)}.
     * @return New protocol object for communication. 
     */
    public abstract JokeProtocol newInstance();
    
}

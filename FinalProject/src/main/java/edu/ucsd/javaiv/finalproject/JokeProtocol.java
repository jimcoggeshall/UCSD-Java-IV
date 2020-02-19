package edu.ucsd.javaiv.finalproject;

/**
 * Interface to an object containing all intelligence required to perform the steps of telling a 
 * joke, including prompting the client if necessary. 
 * 
 * @author jcc
 */
public interface JokeProtocol {
    
    /**
     * Consume a line of input from the client and return a response based on internal state.
     * @param theInput Input string read from the client. 
     * @return Response from the server.
     */
    public String processInput(String theInput);
    
}

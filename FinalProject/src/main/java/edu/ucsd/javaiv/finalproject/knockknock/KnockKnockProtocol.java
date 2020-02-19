package edu.ucsd.javaiv.finalproject.knockknock;

import edu.ucsd.javaiv.finalproject.JokeProtocol;
import edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJokeHandler;

/**
 * Class containing all intelligence required to perform the steps of telling a knock-knock joke,
 * including prompting the client if necessary. 
 * <p>
 * The server owns a reference to a {@link edu.ucsd.javaiv.finalproject.JokeProtocolFactory} whose 
 * purpose is to create instances of these objects to communicate with client threads. In addition, 
 * this class contains its own reference to a
 * {@link edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJokeHandler} object, in order to 
 * maintain its own state while iterating through the list of jokes. 
 * 
 * @author jcc
 */
public class KnockKnockProtocol implements JokeProtocol {

    private static enum Status {
        WAITING, SENTKNOCKKNOCK, SENTCLUE, ANOTHER
    }

    private Status status = Status.WAITING;
    private final ClueAndAnswerJokeHandler jokeHandler;

    /**
     * Create a new instance using the specified handler. Normally objects of this class should be 
     * created via {@link edu.ucsd.javaiv.finalproject.knockknock.KnockKnockProtocolFactory#newInstance()}.
     * 
     * @param jokeHandler Handler object that knows about the jokes and how to iterate through them.
     */
    public KnockKnockProtocol(ClueAndAnswerJokeHandler jokeHandler) {
        this.jokeHandler = jokeHandler;
    }

    /**
     * Consume a line of input from the client and return a response based on internal state.
     * @param theInput Input string read from the client. 
     * @return Response from the server.
     */
    @Override
    public String processInput(String theInput) {
        if (jokeHandler == null) {
            return "Seems I was not initialized properly. Try restarting the server.";
        }
        String theOutput = null;
        theInput = theInput.trim();
        if (null != status) {
            switch (status) {
                case WAITING:
                    theOutput = "Knock! Knock!";
                    status = Status.SENTKNOCKKNOCK;
                    break;
                case SENTKNOCKKNOCK:
                    if (theInput.equalsIgnoreCase("Who's there?")) {
                        theOutput = jokeHandler.getClue();
                        status = Status.SENTCLUE;
                    } else {
                        theOutput = "You're supposed to say \"Who's there?\"! "
                                + "Try again. Knock! Knock!";
                    }
                    break;
                case SENTCLUE:
                    if (theInput.equalsIgnoreCase(jokeHandler.getClue() + " who?")) {
                        theOutput = jokeHandler.getAnswer() + " ...want another? (y/n)";
                        status = Status.ANOTHER;
                    } else {
                        theOutput = "You're supposed to say \""
                                + jokeHandler.getClue() + " who?\""
                                + "! Try again. Knock! Knock!";
                        status = Status.SENTKNOCKKNOCK;
                    }
                    break;
                case ANOTHER:
                    if (theInput.equalsIgnoreCase("y")) {
                        theOutput = "Knock! Knock!";
                        jokeHandler.retrieveNewJoke();
                        status = Status.SENTKNOCKKNOCK;
                    } else {
                        theOutput = "Bye.";
                        status = Status.WAITING;
                    }
                    break;
                default:
                    break;
            }
        }
        return theOutput;
    }

}

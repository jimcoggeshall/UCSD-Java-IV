package edu.ucsd.javaiv.finalproject.knockknock;

import edu.ucsd.javaiv.finalproject.JokeFormatException;
import edu.ucsd.javaiv.finalproject.JokeHandler;
import edu.ucsd.javaiv.finalproject.JokeProtocol;
import edu.ucsd.javaiv.finalproject.JokeProtocolFactory;
import edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJoke;
import edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJokeHandler;
import java.io.IOException;

/**
 * Class responsible for producing {@link edu.ucsd.javaiv.finalproject.knockknock.KnockKnockProtocol}
 * objects. 
 * <p>
 * The server contains an instance of this factory which is configured via GUI input, then uses 
 * the factory to create new protocol objects necessary for client communication. 
 * 
 * @author jcc
 */
public class KnockKnockProtocolFactory extends JokeProtocolFactory {

    private String inputFilename;

    /**
     * Constructor.
     */
    public KnockKnockProtocolFactory() {}

    /**
     * Set the input file from which to read jokes. 
     * <p>
     * In addition to setting the filename internally, this method will open the file and validate
     * its format (namely, it must be a tab-delimited text file with at least two fields per line). 
     * This is not strictly necessary but it allows for communicating any error-prone state to the
     * EDT prior to attempting to spawn any client threads. Specifically, any exceptions that might
     * be thrown (and suppressed due to the {@link javax.swing.SwingWorker} architecture) in the 
     * {@link edu.ucsd.javaiv.finalproject.JokeServer#doInBackground} method having to do with input
     * validation should be thrown here (on the EDT) first, allowing us to serve error messages and
     * bail out before listening with a faulty configuration. 
     * 
     * @param inputFilename Path to tab-delimited input file (two fields = one joke per line).
     * @throws IOException If the file cannot be read.
     * @throws JokeFormatException If the file is formatted incorrectly. 
     */
    @Override
    public void setJokesFile(String inputFilename) throws IOException, JokeFormatException {
        this.inputFilename = inputFilename;
        ClueAndAnswerJokeHandler inputFileTestHandler = new ClueAndAnswerJokeHandler() {
            @Override
            public void validateClueAndAnswerJoke(ClueAndAnswerJoke joke) throws JokeFormatException {
                String jokeClue = joke.getClue();
                String jokeAnswer = joke.getAnswer();
                if (jokeClue == null || jokeAnswer == null) {
                    throw new JokeFormatException("Invalid knock-knock joke format!");
                }
            }
        };
        inputFileTestHandler.init(inputFilename);
    }

    /**
     * Create a new  {@link edu.ucsd.javaiv.finalproject.knockknock.KnockKnockProtocol} object, 
     * configured to use jokes in the file specified in 
     * {@link edu.ucsd.javaiv.finalproject.knockknock.KnockKnockProtocolFactory#setJokesFile(String)}.
     * @return New protocol object for communication. 
     */
    @Override
    public JokeProtocol newInstance() {
        ClueAndAnswerJokeHandler handler = new ClueAndAnswerJokeHandler();
        try { 
            handler.init(inputFilename);
        } catch (Exception e) {}
        return new KnockKnockProtocol(handler);
    }

}

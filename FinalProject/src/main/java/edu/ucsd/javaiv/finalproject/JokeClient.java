package edu.ucsd.javaiv.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple client for communicating via text with the server over a socket. 
 * <p>
 * Messages are discretized based on the newline character. 
 * 
 * @author jcc
 */
public class JokeClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    /**
     * Constructor. The socket is assumed already to be open (though not bound).
     * <p>
     * The server is assumed to be listening on the other side of the socket. 
     * @param socket Socket on which to communicate. 
     * @throws IOException If retrieving output or input streams from the socket fails. 
     */
    public JokeClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(
            new InputStreamReader(
                socket.getInputStream()
            )
        );
    }

    /**
     * Send a message to the server and get a response back.
     * 
     * @param inputFromGui Message to send to the server.
     * @return Server response.
     */
    public String communicateWithServer(String inputFromGui) {
        String outputFromServer = "";
        try {
            out.println(inputFromGui);
            outputFromServer = in.readLine();
        } catch (IOException e) {
            Logger.getLogger(JokeClient.class.getName()).log(Level.INFO, null, e);
        }
        return outputFromServer;
    }

}

package edu.ucsd.javaiv.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 * Class representing a server daemon. This class is responsible for opening and maintaining the 
 * server socket, holding the {@link edu.ucsd.javaiv.finalproject.JokeProtocolFactory}, and 
 * starting new threads to communicate with new incoming client connections. 
 * 
 * @author jcc
 */
public class JokeServer extends SwingWorker<String, Object> {

    private final SocketAddress socketAddress;
    private final ServerSocket socket;
    private final JokeProtocolFactory jokeProtocolFactory;
    
    private List<JokeServerInstance> instanceRegistry;
    private List<Thread> threadRegistry;

    /**
     * Start the server on the given port and maintain a reference to the {@link edu.ucsd.javaiv.finalproject.JokeProtocolFactory}.
     * 
     * @param jokeProtocolFactory Factory object used to create {@link edu.ucsd.javaiv.finalproject.JokeProtocol} objects for new connections.
     * @param socketPort Port for the server to listen on. 
     * @throws IOException If the socket cannot be bound to the specified port. 
     */
    public JokeServer(JokeProtocolFactory jokeProtocolFactory, int socketPort) throws IOException {
        this.socketAddress = new InetSocketAddress(socketPort);
        this.socket = new ServerSocket();
        this.jokeProtocolFactory = jokeProtocolFactory;
        this.threadRegistry = new ArrayList<>();
        socket.bind(socketAddress);
    }
    
    /**
     * Get the local address of the server socket. 
     * @return Server socket address.
     */
    public SocketAddress getSocketAddress() {
        return socket.getLocalSocketAddress();
    }
    
    /**
     * Get the port to which the server socket is bound. 
     * @return Server port.
     */
    public int getSocketPort() {
        return socket.getLocalPort();
    }

    /**
     * Entry point to run as daemon. Listen for incoming connections and start a new thread when 
     * one arrives. 
     * <p>
     * Should generally by run via {@link javax.swing.SwingWorker#execute()}.
     * @return Empty string. 
     */
    @Override
    protected String doInBackground() {
        try {   
            while (true) {
                Socket clientSocket = socket.accept();
                JokeProtocol protocol = jokeProtocolFactory.newInstance();
                JokeServerInstance server = new JokeServerInstance(protocol, clientSocket);
                Thread serverThread = new Thread(server);
                threadRegistry.add(serverThread);
                serverThread.start();
            } 
        } catch (IOException e) {
            this.cleanup();
        }
        return "";
    }
    
    /**
     * Attempt to destroy this server object nicely by terminating all server threads and closing 
     * the socket.
     */
    public void cleanup() {
        for (Thread t : threadRegistry) {
            t.interrupt();
        }
        try {
            socket.close();
        } catch (IOException e) {}
    }

    private class JokeServerInstance implements Runnable {

        private final JokeProtocol jokeProtocol;
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;

        /**
         * Instance of a thread to communicate with a connection. 
         * <p>
         * Each client connection should have its own object of this type and 
         * should run in its own thread.
         * @param jokeProtocol This thread's protocol for client communication. 
         * @param socket The socket on which this thread will communicate. 
         * @throws IOException If retrieving output or input streams from the socket fails. 
         */
        public JokeServerInstance(JokeProtocol jokeProtocol, Socket socket) throws IOException {
            this.jokeProtocol = jokeProtocol;
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()
                )
            );
        }

        /**
         * Communicate over the socket specified in the constructor, using the protocol specified 
         * in the constructor.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    String queryFromClient = in.readLine();
                    String response = jokeProtocol.processInput(queryFromClient);
                    if (Thread.currentThread().isInterrupted()) {
                        socket.close();
                        break;
                    }
                    out.println(response);
                }
            } catch (IOException e) {
                Logger.getLogger(JokeServerInstance.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

}

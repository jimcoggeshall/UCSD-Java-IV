package edu.ucsd.javaiv.finalproject.knockknock;

import edu.ucsd.javaiv.finalproject.JokeClient;
import edu.ucsd.javaiv.finalproject.JokeFormatException;
import edu.ucsd.javaiv.finalproject.JokeProtocolFactory;
import edu.ucsd.javaiv.finalproject.JokeServer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Entry point to the GUI-based multithreaded knock-knock server.
 * <p>
 * This class manages a swing GUI with three fundamental states:
 * <ul>
 * <li> Neither the client nor the server is running. Clients cannot be started from the GUI. 
 * <li> The server is running and reports the port to which it's bound. Clients may be started from the GUI.
 * <li> The server is running and one or more client is connected. 
 * </ul>
 * <p>
 * The main GUI window serves as a proxy to a server daemon process, by which the process may be 
 * configured, started, and stopped an arbitrary number of times. Applicable configurations for the
 * server include the input file from which jokes are to be read (mandatory) and the port on which
 * the server should listen for client connections (optional). 
 * <p>
 * After the server is started, clients may be started via the GUI. In case the server port was 
 * chosen automatically, the actual port is displayed in the input field. 
 * <p>
 * Multiple clients may simultaneously connect to the server. Each client is given a different 
 * random joke to begin with, and subsequent jokes are read sequentially according to the input 
 * file. Although no runtime option exists to alter the server behavior to read jokes in a 
 * completely random order, one may alter {@link edu.ucsd.javaiv.finalproject.clueandanswer.ClueAndAnswerJokeHandler} to use 
 * {@link edu.ucsd.javaiv.finalproject.RandomJokeStrategy} rather than the default {@link edu.ucsd.javaiv.finalproject.CircularJokeStrategy} .
 * 
 * 
 * @author jcc
 */
public class KnockKnockApplication {

    private static final String SRV_STOPPED_TXT = "Start Server";
    private static final String SRV_RUNNING_TXT = "Stop Server";
    private static final String CLIENT_TXT = "Create New Client";
    
    private static final String INPUT_LABEL = "Input file: ";
    private static final String DEFAULT_FILE = "src/main/resources/kk_jokes.tsv";
    private static final String PORT_LABEL = "Server port: ";
    private static final String DEFAULT_PORT = "Random";
    
    private static final JButton serverButton = new JButton(SRV_STOPPED_TXT);
    private static final JButton createClientButton = new JButton(CLIENT_TXT);
    private static final JTextField jokesFileInputField = new JTextField(DEFAULT_FILE);
    private static final JLabel jokesFileInputLabel = new JLabel(INPUT_LABEL);
    private static final JTextField jokesServerPortField = new JTextField(DEFAULT_PORT);
    private static final JLabel jokesServerPortLabel = new JLabel(PORT_LABEL);

    private static final JokeProtocolFactory jokeProtocolFactory = new KnockKnockProtocolFactory();
    private static int socketPort;
    
    private JokeServer serverThread = null;
    
    private void errorDialog(String message) {
        JOptionPane.showMessageDialog(null,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void warningDialog(String message) {
        JOptionPane.showMessageDialog(null,
            message,
            "Warning",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private boolean serverRunning() {
        if (serverThread != null) {
            return !serverThread.isDone();
        }
        return false;
    }
    
    private void startServer() throws IOException {
        try {
            socketPort = Integer.parseInt(jokesServerPortField.getText());
        } catch (NumberFormatException e) {
            socketPort = 0;
        }
        serverThread = new JokeServer(jokeProtocolFactory, socketPort);
        jokesServerPortField.setText(Integer.toString(serverThread.getSocketPort()));
        jokesServerPortField.setEditable(false);
        serverThread.execute();
    }
    
    private void stopServer() {
        jokesServerPortField.setText(DEFAULT_PORT);
        jokesServerPortField.setEditable(true);
        jokesFileInputField.setEditable(true);
        serverThread.cleanup();
        serverThread.cancel(true);
        serverThread = null;
    }

    private class ServerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (SRV_STOPPED_TXT.equals(serverButton.getText())) {
                if (serverRunning()) {
                    stopServer();
                }
                
                String jokesFile = jokesFileInputField.getText();
                try {
                    jokeProtocolFactory.setJokesFile(jokesFile);
                } catch (JokeFormatException ee) {
                    errorDialog("Jokes file " + jokesFile + " has invalid format.");
                    return;
                } catch (IOException ee) {
                    errorDialog("Could not read jokes file " + jokesFile + ".");
                    return;
                }
                
                try {
                    startServer();
                    serverButton.setText(SRV_RUNNING_TXT);
                    createClientButton.setEnabled(true);
                } catch (IOException ee) {
                    warningDialog("Could not start server.");
                    stopServer();
                    return;
                }
                jokesFileInputField.setEditable(false);
                
            } else if (SRV_RUNNING_TXT.equals(serverButton.getText())) {
                stopServer();
                serverButton.setText(SRV_STOPPED_TXT);
                createClientButton.setEnabled(false);
            }
        }
    }
    
    private class CreateClientButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!serverRunning()) {
                throw new RuntimeException("Server is not running! The " + CLIENT_TXT + " button already should be deactivated.");
            }
            SocketAddress address = serverThread.getSocketAddress();
            Socket clientSocket = new Socket();
            try {
                clientSocket.connect(address);
                final JokeClient client = new JokeClient(clientSocket);
                
                JFrame clientFrame = new JFrame("KnockKnockClient");
                Container clientPane = clientFrame.getContentPane();
                SpringLayout layout = new SpringLayout();
                
                final JTextArea clientTextArea = new JTextArea("Server: " + client.communicateWithServer("") + "\n");
                clientTextArea.setEditable(false);
                final JTextField clientTextField = new JTextField();
                final JButton clientCommunicateButton = new JButton();

                clientTextField.setText("");

                clientCommunicateButton.setText("Send");

                clientFrame.setSize(500, 300);
                clientFrame.setLayout(layout);
                layout.minimumLayoutSize(clientFrame);
                clientPane.add(clientTextArea);
                clientPane.add(clientTextField);
                clientPane.add(clientCommunicateButton);
                
                layout.putConstraint(SpringLayout.NORTH, clientTextArea, 5, SpringLayout.NORTH, clientPane);
                layout.putConstraint(SpringLayout.WEST, clientTextArea, 5, SpringLayout.WEST, clientPane);                
                layout.putConstraint(SpringLayout.EAST, clientPane, 5, SpringLayout.EAST, clientTextArea);
                layout.putConstraint(SpringLayout.NORTH, clientTextField, 5, SpringLayout.SOUTH, clientTextArea);
                layout.putConstraint(SpringLayout.WEST, clientTextField, 0, SpringLayout.WEST, clientTextArea);
                layout.putConstraint(SpringLayout.NORTH, clientCommunicateButton, 0, SpringLayout.NORTH, clientTextField);
                layout.putConstraint(SpringLayout.EAST, clientCommunicateButton, -5, SpringLayout.EAST, clientPane);                
                layout.putConstraint(SpringLayout.EAST, clientTextField, -5, SpringLayout.WEST, clientCommunicateButton);
                layout.putConstraint(SpringLayout.SOUTH, clientPane, 5, SpringLayout.SOUTH, clientCommunicateButton);

                
                clientCommunicateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ee) {
                        String input = clientTextField.getText();
                        clientTextField.setText("");
                        clientTextArea.append("Client: " + input + "\n");
                        String output = client.communicateWithServer(input);
                        if (output == null) {
                            clientTextArea.append("No response from server. Ensure server is running and open a new client.\n");
                        } else {
                            clientTextArea.append("Server: " + output + "\n");
                        }
                    }
                });

                clientTextField.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            clientCommunicateButton.doClick();
                        }
                    }                
                });

                clientFrame.setVisible(true);

            } catch (IOException ex) {
                Logger.getLogger(KnockKnockApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Initialize and configure components of the main GUI window responsible for launching threads.
     * 
     * @return JPanel containing components.
     */
    public Component createComponents() {
        jokesFileInputLabel.setToolTipText("Path to input tab-delimited jokes file.");
        jokesServerPortLabel.setToolTipText("Port number to be used by server. If not specified a random port will be chosen.");
        serverButton.setMnemonic(KeyEvent.VK_I);
        serverButton.addActionListener(new ServerButtonListener());
        createClientButton.setMnemonic(KeyEvent.VK_I);
        createClientButton.addActionListener(new CreateClientButtonListener());
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane = new JPanel(gridbag);
        c.insets = new Insets(10, 0, 0, 10);
        c.gridx = 0;
        c.gridy = 0;
        pane.add(jokesFileInputLabel, c);
        c.gridx++;
        c.ipadx = 50;
        pane.add(jokesFileInputField, c);
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        pane.add(jokesServerPortLabel, c);
        c.gridx++;
        c.ipadx = 50;
        pane.add(jokesServerPortField, c);
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        pane.add(serverButton, c);
        c.gridx++;
        pane.add(createClientButton, c);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return pane;
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("KnockKnockApplication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KnockKnockApplication app = new KnockKnockApplication();
        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Main entry point. No command-line arguments are supported, as all configuration is managed 
     * via the main UI window. 
     * 
     * @param args None supported.
     */
    public static void main(String[] args) {
        createClientButton.setEnabled(false);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

import edu.ucsd.examples.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author jcc
 */
public class TestGC {

    private int numClicks = 0;
    private static long start;
    private static long stop;

    private final JLabel label = new JLabel("<html>Begin timing.  Run jps to find lmvid, then run visualgc.</html>");

    private class TestGCButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            start = System.currentTimeMillis();
            for (int i = 0; i < 50000; i++) {
                new Rock();
            }
            stop = System.currentTimeMillis();
            numClicks++;
            label.setText("<html>remaining Rocks = " + Rock.getLiveRocks()
                    + "; loop time = " + (stop - start)
                    + "; total clicks = " + numClicks
                    + "</html>");
        }
    }

    public Component createComponents() {
        JButton button = new JButton("Press to create 50K Rocks");
        button.setMnemonic(KeyEvent.VK_I);
        button.addActionListener(new TestGCButtonListener());
        label.setLabelFor(button);
        JPanel pane = new JPanel(new GridLayout(0, 1));
        pane.add(button);
        pane.add(label);
        pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
        );

        return pane;
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("TestGCSwingApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TestGC app = new TestGC();
        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

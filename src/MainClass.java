import javax.swing.SwingUtilities;
import gui.CalculatorFrame;

/**
 * MainClass.
 */
public class MainClass {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final CalculatorFrame frame = new CalculatorFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

    }
}
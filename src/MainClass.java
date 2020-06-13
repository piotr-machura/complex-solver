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
                CalculatorFrame.getInstance().setLocationRelativeTo(null);
                CalculatorFrame.getInstance().setVisible(true);
            }
        });

    }
}
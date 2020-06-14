import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import algorithm.solver.Solver;
import gui.CalculatorFrame;

/**
 * MainClass.
 *
 * @Author Piotr Machura
 */
public class MainClass {
    public static void main(final String[] args) {
        /** Set custom look and feel */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            /** The look and feel is not supported -> do nothing */
        }
        /** Get config file from cwd */
        File solverrc = new File(System.getProperty("user.dir") + "\\.solverrc");
        try {
            Solver.readConfig(solverrc);
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) {
                /** If there is a problem with the file other than it not existing delete it */
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null,
                                "The .solverrc file has been corrupted:\n\n" + e.getMessage()
                                        + "\n\nand will now be deleted. Defaults restored.",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                });
                solverrc.delete();
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CalculatorFrame.getInstance().setLocationRelativeTo(null);
                CalculatorFrame.getInstance().setVisible(true);
                CalculatorFrame.getInstance().setState(CalculatorFrame.NORMAL);
                CalculatorFrame.getInstance().toFront();
            }
        });
    }
}
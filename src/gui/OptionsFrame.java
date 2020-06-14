package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.*;
import java.util.HashMap;
import java.awt.FlowLayout;
import java.awt.Toolkit;

/**
 * The singleton class OptionsFrame.
 *
 * Creates and modifies the .solverrc file, allowing for changes in the solver
 * behaviour.
 *
 * @Author Piotr Machura
 */
public class OptionsFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The singleton instance */
    private static OptionsFrame instance = new OptionsFrame();

    /**
     * getInstance.
     *
     * @return the singleton instance of OptionsFrame
     */
    public static OptionsFrame getInstance() {
        return instance;
    }

    /** GUI elements */
    JLabel warningLabel;
    HashMap<String, JLabel> optionLabels;
    HashMap<String, JTextField> optionFields;
    JButton applyButton, restoreButton;

    private OptionsFrame() {
        super("Options");
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("cIcon.png")));
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}
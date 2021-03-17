package gui;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The class CalcButton.
 *
 * Autmatically sets up actionListener and actionCommand based on it's name.
 *
 * @Author Piotr Machura
 */
public class CalcButton extends JButton {
    private static final long serialVersionUID = 1L;

    public CalcButton(String name, ActionListener acListener) {
        super(name);
        this.setActionCommand(name);
        this.addActionListener(acListener);
        this.setFont(new Font("Sans Serrif", Font.BOLD, 14));
    }

}
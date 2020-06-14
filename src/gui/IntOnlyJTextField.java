package gui;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 * The class IntOnlyJTextField.
 *
 * A JTextField which only allows an integer input
 *
 * @Author Piotr Machura
 */
public class IntOnlyJTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    public IntOnlyJTextField(String title) {
        super(title);
    }

    public IntOnlyJTextField(int columns) {
        super(columns);
    }

    /** If keypress is not digit/backspace/delete -> do nothing */
    @Override
    public void processKeyEvent(KeyEvent ev) {
        Character c = ev.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            ev.consume();
        } else {
            super.processKeyEvent(ev);
            ev.consume();
        }
        return;
    }
}
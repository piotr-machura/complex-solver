package gui;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 * The class FloatOnlyJTextField.
 *
 * A JTextField which only allows a floating point number input
 *
 * @Author Piotr Machura
 */
public class FloatOnlyJTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    public FloatOnlyJTextField(String title) {
        super(title);
    }

    public FloatOnlyJTextField(int columns) {
        super(columns);
    }

    /** If keypress is not digit/backspace/delete/dot/enter -> do nothing */
    @Override
    public void processKeyEvent(KeyEvent ev) {
        Character c = ev.getKeyChar();
        final int ENTER_KECODE = 10;
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c.equals('.')
                || c == ENTER_KECODE)) {
            ev.consume();
        } else {
            if (c.equals('.') && this.getText().contains(".")) {
                /** There can only be one dot */
                ev.consume();
            }
            super.processKeyEvent(ev);
            ev.consume();
        }
        return;
    }
}
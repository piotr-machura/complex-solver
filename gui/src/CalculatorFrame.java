package gui.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.HashMap;

/*
 * CalculatorFrame v0.1.2
 */
public class CalculatorFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /* Panels */
    JPanel upperPanel, centerPanel, bottomPanel;
    JPanel buttonsContainer;

    /* Buttons */
    JButton[] nmbButtons;
    HashMap<String, JButton> fnButtons;
    HashMap<String, JButton> opButtons;
    JButton solveButton;

    /* Other elements */
    JTextField funcInput;
    Font mathFont;
    private static ImageIcon ICON = new ImageIcon("/cIcon.png"); //! Nie działa

    /* String to be passed further as function f(z) */
    String fz;
    int accuracy;
    int sizeOfRect;

    final static int DEF_ACCURACY = 1;
    final static int DEF_SIZEOFRECT = 10;

    public CalculatorFrame() throws HeadlessException {
        /* Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setResizable(false);
        this.setTitle("Complex function grapher");

        this.setIconImage(ICON.getImage());

        /* Initializing panels */
        centerPanel = new JPanel();
        buttonsContainer = new JPanel();
        upperPanel = new JPanel();
        bottomPanel = new JPanel();

        /* Setting up input text field */
        funcInput = new JTextField();
        funcInput.setPreferredSize(new Dimension(450, 50));
        mathFont = new Font("SansSerif", Font.PLAIN, 24);
        funcInput.setFont(mathFont);

        /* Press enter to solve */
        funcInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "solve"));
                }
            }

            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
            }

        });

        /* Adding function input to upper panel */
        upperPanel.setLayout(new FlowLayout());
        upperPanel.add(funcInput);

        /* Setting up calculator buttons */

        /* Numbers 0-9 */
        nmbButtons = new JButton[10];
        for (int i = 0; i < nmbButtons.length; i++) {
            nmbButtons[i] = new JButton("" + i);
            /* Add action commands and wire up action listener */
            nmbButtons[i].setActionCommand("" + i);
            nmbButtons[i].addActionListener(this);
        }

        /* Operator buttons. Each buttons "name" and function is its hash key */
        opButtons = new HashMap<String, JButton>();
        opButtons.put("z", new JButton("z"));
        opButtons.put("i", new JButton("i"));
        opButtons.put("e", new JButton("e"));
        opButtons.put(".", new JButton("."));
        opButtons.put("+", new JButton("+"));
        opButtons.put("-", new JButton("-"));
        opButtons.put("*", new JButton("*"));
        opButtons.put("/", new JButton("/"));
        opButtons.put("^", new JButton("^"));
        opButtons.put("(", new JButton("("));
        opButtons.put(")", new JButton(")"));
        opButtons.put("CE", new JButton("CE"));
        opButtons.put("back", new JButton("<-"));

        /* Add action commands and wire up action listener */
        for (final String opKey : opButtons.keySet()) {
            opButtons.get(opKey).setActionCommand(opKey);
            opButtons.get(opKey).addActionListener(this);
        }

        /* Function buttons. Each buttons "name" and function is its hash key */
        fnButtons = new HashMap<String, JButton>();
        fnButtons.put("ln", new JButton("ln"));
        fnButtons.put("sin", new JButton("sin"));
        fnButtons.put("cos", new JButton("cos"));
        fnButtons.put("tan", new JButton("tan"));
        fnButtons.put("sinh", new JButton("sinh"));
        fnButtons.put("cosh", new JButton("cosh"));

        /* Add action commands and wire up action listener */
        for (final String fnKey : fnButtons.keySet()) {
            fnButtons.get(fnKey).setActionCommand(fnKey);
            fnButtons.get(fnKey).addActionListener(this);
        }

        /* Arranging buttons in button container in respective order */
        buttonsContainer.setLayout(new GridLayout(0, 5, 5, 5));
        for (int i = 1; i < 4; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(opButtons.get("back"));
        buttonsContainer.add(opButtons.get("CE"));
        for (int i = 4; i < 7; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(opButtons.get("e"));
        buttonsContainer.add(fnButtons.get("sin"));
        for (int i = 7; i < 10; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(fnButtons.get("ln"));
        buttonsContainer.add(fnButtons.get("cos"));
        buttonsContainer.add(opButtons.get("."));
        buttonsContainer.add(nmbButtons[0]);
        buttonsContainer.add(opButtons.get("z"));
        buttonsContainer.add(opButtons.get("i"));
        buttonsContainer.add(fnButtons.get("tan"));
        buttonsContainer.add(opButtons.get("+"));
        buttonsContainer.add(opButtons.get("-"));
        buttonsContainer.add(opButtons.get("*"));
        buttonsContainer.add(opButtons.get("/"));
        buttonsContainer.add(opButtons.get("^"));
        buttonsContainer.add(opButtons.get("("));
        buttonsContainer.add(opButtons.get(")"));
        buttonsContainer.add(fnButtons.get("sinh"));
        buttonsContainer.add(fnButtons.get("cosh"));

        /* Adding button container to center panel */
        buttonsContainer.setPreferredSize(new Dimension(350, 325));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(buttonsContainer, BorderLayout.CENTER);

        /* Setting up bottom panel */
        solveButton = new JButton("Solve");
        solveButton.setPreferredSize(new Dimension(140, 50));
        solveButton.setActionCommand("solve");
        solveButton.addActionListener(this);
        bottomPanel.add(solveButton);

        /* Adding main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String buttonID = e.getActionCommand();

        int caretPosition = funcInput.getCaretPosition();
        String currentText = funcInput.getText();
        String beforeCaret = currentText.substring(0, caretPosition);
        String afterCaret = currentText.substring(caretPosition, currentText.length());

        switch (buttonID) {

            case "solve":
                /* Set fn equal to input field and attempt to fix it to meet the standards */
                this.fz = funcInput.getText();

                // TODO: attemptFix()
                /*
                 * Create a seperate function attemptFix() to fix missing brackets, missing "*",
                 * notify about inputs which are not fuctions, notify about invalid inputs etc.
                 */
                long countOBr = fz.chars().filter(ch -> ch == '(').count();
                long countCBr = fz.chars().filter(ch -> ch == ')').count();
                if (countOBr > countCBr) {
                    while (countOBr > countCBr) {
                        this.fz += ")";
                        countCBr += 1;
                    }
                    funcInput.setText(this.fz);
                    JOptionPane.showMessageDialog(null, "There was an attempt at fixing: missing brackets", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                FunctionFrame fFrame = new FunctionFrame(fz, DEF_ACCURACY, DEF_SIZEOFRECT);
                fFrame.setVisible(true);
                break;

            case "CE":
                /* Clear text field and refocus */
                funcInput.setText("");
                funcInput.requestFocus();
                break;

            case "back":
                /*
                 * Delete character before caret. Edge case: caret at the beginning -> do
                 * nothing
                 */
                // TODO: if in fron of a function (ex. sin()) make it delete the whole function
                if (caretPosition == 0) {
                    funcInput.requestFocus();
                    break;
                }

                funcInput.setText(beforeCaret.substring(0, beforeCaret.length() - 1) + afterCaret);

                /* Refocus on text field and move caret to the left */
                funcInput.requestFocus();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.setCaretPosition(caretPosition - 1);
                    }
                });
                break;

            default:
                /* Puts buttonID at caret. Adds an opening bracket for functions. */
                String putAtCaret = buttonID;
                if (fnButtons.keySet().contains(buttonID)) {
                    putAtCaret += "(";
                }
                String newText = beforeCaret + putAtCaret + afterCaret;
                funcInput.setText(newText);

                /* Refocus on text field and move caret to the right */
                funcInput.requestFocus();
                final int moveCaretBy = putAtCaret.length();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.setCaretPosition(caretPosition + moveCaretBy);
                    }
                });
                break;
        }
    }

    /* Test */
    public static void main(final String[] args) {
        final CalculatorFrame frame = new CalculatorFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
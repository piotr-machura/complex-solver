package gui.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * CalculatorFrame v0.0.2
 */
public class CalculatorFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    // Panels
    JPanel upperPanel, centerPanel, bottomPanel;
    JPanel buttonsContainer;

    // Buttons
    JButton[] nmbButtons;
    HashMap<String, JButton> fnButtons;
    HashMap<String, JButton> opButtons;
    JButton solveButton;

    // Other elements
    JTextField funcInput;
    Font mathFont;

    public CalculatorFrame() throws HeadlessException {
        /*
         * Basic parameters
         */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setResizable(false);
        this.setTitle("Complex function grapher");

        /*
         * Initializing panels
         */
        centerPanel = new JPanel();
        buttonsContainer = new JPanel();
        upperPanel = new JPanel();
        bottomPanel = new JPanel();

        /*
         * Setting up input text field
         */
        funcInput = new JTextField();
        funcInput.setPreferredSize(new Dimension(450, 50));
        mathFont = new Font("SansSerif", Font.ITALIC, 24);
        funcInput.setFont(mathFont);

        // Press enter to solve
        funcInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    // ! Placeholder response
                    System.out.println("enter to solve when in textfield");
                }
            }

            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
            }

        });

        // Adding function input to upper panel
        upperPanel.setLayout(new FlowLayout());
        upperPanel.add(funcInput);

        /*
         * Setting up calculator buttons
         */

        // Numbers 0-9
        nmbButtons = new JButton[10];
        for (int i = 0; i < nmbButtons.length; i++) {
            nmbButtons[i] = new JButton("" + i);
            nmbButtons[i].setActionCommand("" + i);
            nmbButtons[i].addActionListener(this);
        }

        // Operator buttons. Each buttons "name" and function is its hash key
        opButtons = new HashMap<String, JButton>();
        opButtons.put("z", new JButton("z"));
        opButtons.put(",", new JButton(","));
        opButtons.put("+", new JButton("+"));
        opButtons.put("-", new JButton("-"));
        opButtons.put("*", new JButton("*"));
        opButtons.put("/", new JButton(","));
        opButtons.put("^", new JButton("^"));
        opButtons.put("(", new JButton("("));
        opButtons.put(")", new JButton(")"));
        opButtons.put("CE", new JButton("CE"));
        opButtons.put("back", new JButton("<-"));
        for (final String opKey : opButtons.keySet()) {
            opButtons.get(opKey).setActionCommand(opKey);
            opButtons.get(opKey).addActionListener(this);
        }

        // Function buttons. Each buttons "name" and function is its hash key
        fnButtons = new HashMap<String, JButton>();
        fnButtons.put("e", new JButton("e"));
        fnButtons.put("pi", new JButton("pi"));
        fnButtons.put("ln", new JButton("ln"));
        fnButtons.put("sin", new JButton("sin"));
        fnButtons.put("cos", new JButton("cos"));
        fnButtons.put("tan", new JButton("tan"));
        fnButtons.put("sinh", new JButton("sinh"));
        fnButtons.put("cosh", new JButton("cosh"));
        for (final String fnKey : fnButtons.keySet()) {
            fnButtons.get(fnKey).setActionCommand(fnKey);
            fnButtons.get(fnKey).addActionListener(this);
        }

        // Arranging buttons in button container (see specification)
        buttonsContainer.setLayout(new GridLayout(0, 5));
        for (int i = 1; i < 4; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(opButtons.get("back"));
        buttonsContainer.add(opButtons.get("CE"));
        for (int i = 4; i < 7; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(fnButtons.get("sin"));
        buttonsContainer.add(fnButtons.get("e"));
        for (int i = 7; i < 10; i++) {
            buttonsContainer.add(nmbButtons[i]);
        }
        buttonsContainer.add(fnButtons.get("cos"));
        buttonsContainer.add(fnButtons.get("ln"));
        buttonsContainer.add(nmbButtons[0]);
        buttonsContainer.add(opButtons.get(","));
        buttonsContainer.add(opButtons.get("z"));
        buttonsContainer.add(fnButtons.get("tan"));
        buttonsContainer.add(fnButtons.get("pi"));
        buttonsContainer.add(opButtons.get("+"));
        buttonsContainer.add(opButtons.get("-"));
        buttonsContainer.add(opButtons.get("*"));
        buttonsContainer.add(opButtons.get("/"));
        buttonsContainer.add(opButtons.get("^"));
        buttonsContainer.add(opButtons.get("("));
        buttonsContainer.add(opButtons.get(")"));
        buttonsContainer.add(fnButtons.get("sinh"));
        buttonsContainer.add(fnButtons.get("cosh"));

        // Adding button container to center panel
        buttonsContainer.setPreferredSize(new Dimension(325, 325));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(buttonsContainer, BorderLayout.CENTER);

        /*
         * Setting up bottom panel
         */
        solveButton = new JButton("Solve");
        solveButton.setPreferredSize(new Dimension(140, 50));
        solveButton.setActionCommand("solve");
        solveButton.addActionListener(this);
        bottomPanel.add(solveButton);

        /*
         * Adding main panels to frame
         */
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
                break;

            case "CE": // Clear input text field
                funcInput.setText("");
                funcInput.requestFocus();
                break;

            case "back": // Delete character before caret
                if (caretPosition == 0) { // Edge case: caret at the beginning -> do nothing
                    funcInput.requestFocus();
                    break;
                }

                funcInput.setText(beforeCaret.substring(0, beforeCaret.length() - 1) + afterCaret);

                // Refocus on text field and move caret to the left
                funcInput.requestFocus();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.setCaretPosition(caretPosition - 1);
                    }

                });
                break;

            default: // Puts buttonID at caret. Adds an opening bracket for functions.
                String putAtCaret = buttonID;
                if (fnButtons.keySet().contains(buttonID) && !buttonID.equals("e") && !buttonID.equals("pi")) {
                    putAtCaret += "(";
                }
                String newText = beforeCaret + putAtCaret + afterCaret;
                funcInput.setText(newText);

                // Refocus on text field and move caret to the right
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

    /*
     * Test
     */
    public static void main(final String[] args) {
        final CalculatorFrame frame = new CalculatorFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
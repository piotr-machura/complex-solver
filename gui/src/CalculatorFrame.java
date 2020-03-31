package gui.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import parser.src.exception.CalculatorException;
import java.util.HashMap;

/*
 * CalculatorFrame v0.5.1
 */
public class CalculatorFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /* Panels */
    JPanel upperPanel, centerPanel, bottomPanel;
    JPanel calcButtonsContainer, rangeContainer, accuracyContainer, solveContainer;

    /* Buttons */
    JButton[] nmbButtons;
    HashMap<String, JButton> fnButtons, opButtons;
    JButton solveButton;
    JRadioButton rangeAuto;
    JComboBox<Integer> accuracyMenu;

    /* Other elements */
    JTextField funcInput, rangeInput;
    JLabel inputLabel, rangeLabel, accMenuLabel;
    Font mathFont;
    Boolean autoWarning; /* Whether to show warning about automatic range */
    private static ImageIcon ICON = new ImageIcon("/cIcon.png"); // ! Nie działa

    /* Arguments to pass further */
    String fz;
    int accuracy;
    int range; /* Range of 0 indicates automatic range */

    public CalculatorFrame() throws HeadlessException {
        /* Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setResizable(false);
        this.setTitle("Complex solver");

        this.setIconImage(ICON.getImage());
        this.range = 10;
        this.accuracy = 1;
        this.autoWarning = true;

        /* Initializing panels */
        centerPanel = new JPanel();
        calcButtonsContainer = new JPanel();
        upperPanel = new JPanel();
        rangeContainer = new JPanel();
        solveContainer = new JPanel();
        accuracyContainer = new JPanel();
        bottomPanel = new JPanel();

        /* Setting up upper panel */
        /* Input text field */
        funcInput = new JTextField();
        funcInput.setMaximumSize(new Dimension(520, 100));
        mathFont = new Font("SansSerif", Font.PLAIN, 24);
        funcInput.setFont(mathFont);

        /* Press enter to solve */
        funcInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) { /* <- keycode for "Enter" key */
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
        upperPanel.setLayout(new BoxLayout(upperPanel, 1));

        /* Label */
        inputLabel = new JLabel("Input your function here");
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        upperPanel.add(inputLabel);
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

        /* Operator buttons. Each buttons' function is its hash key */
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
        calcButtonsContainer.setLayout(new GridLayout(0, 5, 5, 5));
        for (int i = 1; i < 4; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(opButtons.get("back"));
        calcButtonsContainer.add(opButtons.get("CE"));
        for (int i = 4; i < 7; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(opButtons.get("e"));
        calcButtonsContainer.add(fnButtons.get("sin"));
        for (int i = 7; i < 10; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(fnButtons.get("ln"));
        calcButtonsContainer.add(fnButtons.get("cos"));
        calcButtonsContainer.add(opButtons.get("."));
        calcButtonsContainer.add(nmbButtons[0]);
        calcButtonsContainer.add(opButtons.get("z"));
        calcButtonsContainer.add(opButtons.get("i"));
        calcButtonsContainer.add(fnButtons.get("tan"));
        calcButtonsContainer.add(opButtons.get("+"));
        calcButtonsContainer.add(opButtons.get("-"));
        calcButtonsContainer.add(opButtons.get("*"));
        calcButtonsContainer.add(opButtons.get("/"));
        calcButtonsContainer.add(opButtons.get("^"));
        calcButtonsContainer.add(opButtons.get("("));
        calcButtonsContainer.add(opButtons.get(")"));
        calcButtonsContainer.add(fnButtons.get("sinh"));
        calcButtonsContainer.add(fnButtons.get("cosh"));

        /* Adding button container to center panel */
        calcButtonsContainer.setPreferredSize(new Dimension(430, 315));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(calcButtonsContainer, BorderLayout.CENTER);

        /* Setting up bottom panel */

        /* Solve button */
        solveButton = new JButton("Solve");
        solveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        solveButton.setActionCommand("solve");
        solveButton.addActionListener(this);
        solveButton.setPreferredSize(new Dimension(150, 50));

        solveContainer.setLayout(new FlowLayout(FlowLayout.TRAILING, 63, 10));
        solveContainer.setPreferredSize(new Dimension(400, 80));
        solveContainer.add(solveButton);

        /* Range */

        rangeLabel = new JLabel("Range");
        rangeInput = new JTextField("10") {
            private static final long serialVersionUID = 1L;

            /* If keypress is not digit/backspace/delete -> do nothing */
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
        };
        rangeInput.setPreferredSize(new Dimension(50, 25));
        rangeInput.setEditable(true);
        rangeAuto = new JRadioButton("Auto");
        rangeAuto.setActionCommand("auto");
        rangeAuto.addActionListener(this);
        rangeContainer.setLayout(new GridLayout(3, 1, 0, 5));
        rangeContainer.setPreferredSize(new Dimension(60, 80));
        rangeContainer.add(rangeLabel);
        rangeContainer.add(rangeInput);
        rangeContainer.add(rangeAuto);

        /* Accuracy */
        accMenuLabel = new JLabel("Accuracy");
        Integer[] accuracyMenuContents = { 1, 2, 3, 4 };
        accuracyMenu = new JComboBox<Integer>(accuracyMenuContents);
        accuracyMenu.setBackground(Color.WHITE);

        accuracyContainer.setLayout(new GridLayout(3, 1, 0, 5));
        accuracyContainer.add(accMenuLabel);
        accuracyContainer.add(accuracyMenu);
        accuracyContainer.setPreferredSize(new Dimension(60, 80));

        /* Add containers to bottom panel */
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(solveContainer);
        bottomPanel.add(rangeContainer);
        bottomPanel.add(accuracyContainer);

        /* Adding main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

    }

    /*
     * attemptFix.
     *
     * Attempts to fix missing brackets, missing "*" etc.
     *
     * // TODO: This need to do a lot more than it's doing now.
     */
    void attemptFix() throws CalculatorException {
        if (this.fz.equals("")) {
            throw new CalculatorException("Empty input");
        } else if (!fz.contains("z")) {
            throw new CalculatorException("No variable found");
        }
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
                try {
                    this.attemptFix();
                    this.accuracy = Integer.parseInt(String.valueOf(accuracyMenu.getSelectedItem()));
                    if (!rangeAuto.isSelected()) {
                        /* Throw exception if range is not valid */
                        if (rangeInput.getText().equals("0") || rangeInput.getText().equals("")) {
                            throw new CalculatorException("Incorrect range");
                        }
                        this.range = Integer.parseInt(rangeInput.getText());
                    } else {
                        this.range = 0;
                    }
                    FunctionFrame fFrame = new FunctionFrame(fz, accuracy, range);
                    fFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Provided input is invalid:\n" + exc.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
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
                // TODO: if in front of a function (ex. sin()) make it delete the whole function
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
            case "auto":
                /* Prepare range to be set to 0 and disable input box */
                if (rangeAuto.isSelected()) {
                    rangeInput.setEditable(false);
                    if (autoWarning) {
                        JOptionPane.showMessageDialog(null,
                                "This looks for a rectangle so big it has at least one root inside.\nMight result in extended processing time.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        autoWarning = false;
                    }
                } else {
                    rangeInput.setEditable(true);
                }
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
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import algorithm.solver.SolverAccuracy;
import algorithm.parser.exception.CalculatorException;

/**
 * The class CalculatorFrame
 *
 * @Author Piotr Machura
 */
public class CalculatorFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** Panels */
    JPanel upperPanel, centerPanel, bottomPanel;
    JPanel calcButtonsContainer, rangeContainer, accuracyContainer, solveContainer;

    /** Buttons */
    CalcButton[] nmbButtons;
    HashMap<String, CalcButton> fnButtons, opButtons;
    JButton solveButton;
    JMenuBar upMenu;
    JMenu helpMenu;
    JMenuItem help, credits;
    JRadioButton rangeAuto;
    JComboBox<String> accuracyMenu;

    /** Other elements */
    JTextField funcInput, rangeInput;
    JLabel rangeLabel, accMenuLabel;
    TextPrompt inputPrompt;
    Font mathFont;
    Boolean showAutoWarning;

    /** Arguments to pass further */
    String f_z;
    int range;
    SolverAccuracy acc;

    /**
     * CalculatorFrame constructor.
     */
    public CalculatorFrame() throws HeadlessException {
        /** Basic parameters */
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 530);
        this.setResizable(false);
        this.setTitle("Complex Solver");

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("cIcon.png")));
        this.range = 10;

        /** Initializing panels */
        centerPanel = new JPanel();
        calcButtonsContainer = new JPanel();
        upperPanel = new JPanel();
        rangeContainer = new JPanel();
        solveContainer = new JPanel();
        accuracyContainer = new JPanel();
        bottomPanel = new JPanel();

        /** Set up upper panel */
        /** Menu bar */
        upMenu = new JMenuBar();
        upMenu.setLayout(new FlowLayout(FlowLayout.TRAILING));
        upMenu.setPreferredSize(new Dimension(600, 30));
        helpMenu = new JMenu("Help");
        help = new JMenuItem("Help");
        credits = new JMenuItem("Credits");

        help.setActionCommand("help");
        credits.setActionCommand("credits");

        help.addActionListener(this);
        credits.addActionListener(this);

        helpMenu.add(help);
        helpMenu.add(credits);

        upMenu.add(helpMenu);

        /** Input text field */
        funcInput = new JTextField();
        funcInput.setPreferredSize(new Dimension(600, 50));
        funcInput.setMaximumSize(new Dimension(600, 50));
        mathFont = new Font("SansSerif", Font.PLAIN, 28);
        funcInput.setFont(mathFont);
        funcInput.setHorizontalAlignment(JTextField.CENTER);

        inputPrompt = new TextPrompt("f(z) = 0", funcInput);
        inputPrompt.changeStyle(Font.ITALIC);
        inputPrompt.changeAlpha((float) 0.6);
        inputPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        /** Press enter to solve */
        funcInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) { /** <- keycode for "Enter" key */
                    solveButton.doClick();
                }
            }
        });
        upperPanel.setLayout(new BoxLayout(upperPanel, 1));

        upperPanel.add(upMenu);
        upperPanel.add(funcInput);

        /** Set up calculator buttons */

        /** Numbers 0-9 */
        nmbButtons = new CalcButton[10];
        for (int i = 0; i < nmbButtons.length; i++) {
            nmbButtons[i] = new CalcButton("" + i, this);
        }

        /** Operator buttons */
        opButtons = new HashMap<String, CalcButton>();
        opButtons.put("z", new CalcButton("z", this));
        opButtons.put("i", new CalcButton("i", this));
        opButtons.put("e", new CalcButton("e", this));
        opButtons.put(".", new CalcButton(".", this));
        opButtons.put("+", new CalcButton("+", this));
        opButtons.put("-", new CalcButton("-", this));
        opButtons.put("*", new CalcButton("*", this));
        opButtons.put("/", new CalcButton("/", this));
        opButtons.put("^", new CalcButton("^", this));
        opButtons.put("(", new CalcButton("(", this));
        opButtons.put(")", new CalcButton(")", this));
        opButtons.put("CE", new CalcButton("CE", this));
        opButtons.put("back", new CalcButton("back", this));

        /** Function buttons. Each buttons "name" and function is its hash key */
        fnButtons = new HashMap<String, CalcButton>();
        fnButtons.put("ln", new CalcButton("ln", this));
        fnButtons.put("log", new CalcButton("log", this));
        fnButtons.put("sin", new CalcButton("sin", this));
        fnButtons.put("cos", new CalcButton("cos", this));
        fnButtons.put("sinh", new CalcButton("sinh", this));
        fnButtons.put("cosh", new CalcButton("cosh", this));

        /** Arrange buttons in button container in respective order */
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
        calcButtonsContainer.add(opButtons.get("i"));

        for (int i = 7; i < 10; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(fnButtons.get("sin"));
        calcButtonsContainer.add(fnButtons.get("cos"));
        calcButtonsContainer.add(opButtons.get("."));
        calcButtonsContainer.add(nmbButtons[0]);
        calcButtonsContainer.add(opButtons.get("z"));
        calcButtonsContainer.add(fnButtons.get("ln"));
        calcButtonsContainer.add(fnButtons.get("log"));

        calcButtonsContainer.add(opButtons.get("+"));
        calcButtonsContainer.add(opButtons.get("-"));
        calcButtonsContainer.add(opButtons.get("*"));
        calcButtonsContainer.add(opButtons.get("/"));
        calcButtonsContainer.add(opButtons.get("^"));
        calcButtonsContainer.add(opButtons.get("("));
        calcButtonsContainer.add(opButtons.get(")"));
        calcButtonsContainer.add(fnButtons.get("sinh"));
        calcButtonsContainer.add(fnButtons.get("cosh"));

        /** Add button container to center panel */
        calcButtonsContainer.setPreferredSize(new Dimension(430, 315));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(calcButtonsContainer, BorderLayout.CENTER);

        /** Set up bottom panel */

        /** Solve button */
        solveButton = new JButton("Solve");
        solveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        solveButton.setActionCommand("solve");
        solveButton.addActionListener(this);
        solveButton.setPreferredSize(new Dimension(150, 50));

        solveContainer.setLayout(new FlowLayout(FlowLayout.TRAILING, 63, 10));
        solveContainer.setPreferredSize(new Dimension(415, 80));
        solveContainer.add(solveButton);

        /** Range */

        rangeLabel = new JLabel("Range");
        rangeInput = new JTextField("10") {
            private static final long serialVersionUID = 1L;

            /** If keypress is not digit/backspace/delete/dot -> do nothing */
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
        rangeInput.setPreferredSize(new Dimension(30, 25));
        rangeInput.setEditable(true);
        rangeAuto = new JRadioButton("Auto");
        rangeAuto.setActionCommand("auto");
        showAutoWarning = true;
        rangeAuto.addActionListener(this);
        rangeContainer.setLayout(new GridLayout(3, 1, 0, 5));
        rangeContainer.setPreferredSize(new Dimension(60, 80));
        rangeContainer.add(rangeLabel);
        rangeContainer.add(rangeInput);
        rangeContainer.add(rangeAuto);

        /** Accuracy */
        accMenuLabel = new JLabel("Accuracy");
        String[] accuracyMenuContents = { "LOW", "MED", "HIGH" };
        accuracyMenu = new JComboBox<String>(accuracyMenuContents);
        accuracyMenu.setBackground(Color.WHITE);
        accuracyMenu.setSelectedItem("MED");
        accuracyContainer.setLayout(new GridLayout(3, 1, 0, 5));
        accuracyContainer.add(accMenuLabel);
        accuracyContainer.add(accuracyMenu);
        accuracyContainer.setPreferredSize(new Dimension(60, 80));

        /** Add containers to bottom panel */
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(solveContainer);
        bottomPanel.add(rangeContainer);
        bottomPanel.add(accuracyContainer);

        /** Add main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

    }

    /**
     * validateInput.
     *
     * Prepares the function f_z to be passed further. Sets the accuracy level and
     * checks for closing bracekts.
     */

    private void validateInput() throws CalculatorException {
        if (this.f_z.equals("")) {
            throw new CalculatorException("Empty input");
        } else if (!f_z.contains("z")) {
            throw new CalculatorException("No variable found");
        }
        acc = SolverAccuracy.MED;
        if (accuracyMenu.getSelectedItem().equals("LOW")) {
            acc = SolverAccuracy.LOW;
        } else if (accuracyMenu.getSelectedItem().equals("HIGH")) {
            acc = SolverAccuracy.HIGH;
        }
        if (!rangeAuto.isSelected()) {
            /** Throw exception if range is not valid */
            if (rangeInput.getText().equals("0") || rangeInput.getText().equals("")) {
                throw new CalculatorException("Incorrect range");
            }
            this.range = Integer.parseInt(rangeInput.getText());
        } else {
            this.range = FunctionFrame.AUTO_RANGE;
        }
        /**
         * Brackets
         */
        long countOBr = f_z.chars().filter(ch -> ch == '(').count();
        long countCBr = f_z.chars().filter(ch -> ch == ')').count();
        if (countOBr > countCBr) {
            while (countOBr > countCBr) {
                this.f_z += ")";
                countCBr += 1;
            }
            funcInput.setText(this.f_z);
        }
    }

    /**
     * ActionListener implementation.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String buttonID = e.getActionCommand();

        int caretPosition = funcInput.getCaretPosition();
        String currentText = funcInput.getText();
        String beforeCaret = currentText.substring(0, caretPosition);
        String afterCaret = currentText.substring(caretPosition, currentText.length());

        switch (buttonID) {
            case "credits":
                JOptionPane.showMessageDialog(null, "Mady by:\nPiotr Machura ID 298 183\nKacper Ledwosiński ID 298179",
                        "Credits", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "help":
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        /** Invoke help window if not visible, bring it up if closed */
                        if (Help.getInstance().isVisible() == false) {
                            Help.getInstance().setVisible(true);
                        } else {
                            Help.getInstance().requestFocus();
                            Help.getInstance().setState(JFrame.NORMAL);
                            Help.getInstance().toFront();
                        }
                    }
                });

                break;

            case "solve":
                /** Set fz equal to input field and attempt to fix it to meet the standards */
                this.f_z = funcInput.getText();
                try {
                    this.validateInput();
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Provided input is invalid:\n" + exc.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                }
                FunctionFrame fFrame = new FunctionFrame(f_z, acc, range);
                fFrame.setVisible(true);
                break;

            case "CE":
                /** Clear text field and refocus */
                funcInput.setText("");
                funcInput.requestFocus();
                break;

            case "back":
                /**
                 * Delete character before caret. Edge case: caret at the beginning -> do
                 * nothing
                 */
                if (caretPosition == 0) {
                    funcInput.requestFocus();
                    break;
                }
                funcInput.setText(beforeCaret.substring(0, beforeCaret.length() - 1) + afterCaret);

                /**
                 * Refocus on text field and move caret to the left by the amount of deleted
                 * characters
                 */

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.requestFocus();
                        funcInput.setCaretPosition(caretPosition - 1);
                    }
                });
                break;
            case "auto":
                /** Warn the user and disable input box */
                if (rangeAuto.isSelected()) {
                    rangeInput.setEditable(false);
                    if (showAutoWarning) {
                        Object[] choiceOptions = { "Ok", "Do not show again" };
                        String autoWarningMessage = "This will start with range 5 and automatically enlarge it until it finds a root or reaches 100.";
                        autoWarningMessage += "\nMight result in extended processing time.";
                        /** showOptionDialog will return 1 if "Do not show again" button is clicked */
                        showAutoWarning = (JOptionPane.showOptionDialog(null, autoWarningMessage, "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, choiceOptions,
                                choiceOptions[1]) != 1);
                    }
                } else {
                    rangeInput.setEditable(true);
                }
                break;

            default:
                /** Puts buttonID at caret. Adds an opening bracket for functions. */
                String putAtCaret = buttonID;
                if (fnButtons.keySet().contains(buttonID)) {
                    putAtCaret += "(";
                }
                String newText = beforeCaret + putAtCaret + afterCaret;
                funcInput.setText(newText);

                /** Refocus on text field and move caret to the right */

                final int moveCaretBy = putAtCaret.length();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.requestFocus();
                        funcInput.setCaretPosition(caretPosition + moveCaretBy);
                    }
                });
                break;
        }
    }
}
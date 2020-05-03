/**
 * Made by: Piotr Machura
 */
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
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

import algorithm.Solver.Accuracy;
import parser.exception.CalculatorException;

/**
 * The class CalculatorFrame
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
    JMenu lanMenu, helpMenu;
    JMenuItem en, pl, help, credits;
    JRadioButton rangeAuto;
    JComboBox<String> accuracyMenu;

    /** Other elements */
    JTextField funcInput, rangeInput;
    JLabel rangeLabel, accMenuLabel;
    TextPrompt inputPrompt;
    Font mathFont;

    private static final ImageIcon ICON = new ImageIcon("/cIcon.png"); // ! Nie działa

    /** Arguments to pass further */
    String f_z;
    double range; /* Range of 0 indicates automatic range */

    /**
     * CalculatorFrame constructor.
     */
    public CalculatorFrame() throws HeadlessException {
        /** Basic parameters */
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 530);
        this.setResizable(false);
        this.setTitle("Complex Solver");

        this.setIconImage(ICON.getImage());
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
        lanMenu = new JMenu("Language");
        helpMenu = new JMenu("Help");
        en = new JMenuItem("English");
        pl = new JMenuItem("Polski");
        help = new JMenuItem("Help");
        credits = new JMenuItem("Credits");

        en.setActionCommand("en");
        pl.setActionCommand("pl");
        help.setActionCommand("help");
        credits.setActionCommand("credits");

        en.addActionListener(this);
        pl.addActionListener(this);
        help.addActionListener(this);
        credits.addActionListener(this);

        lanMenu.add(en);
        lanMenu.add(pl);
        helpMenu.add(help);
        helpMenu.add(credits);

        upMenu.add(lanMenu);
        upMenu.add(helpMenu);

        /** Input text field */
        funcInput = new JTextField();
        funcInput.setPreferredSize(new Dimension(600, 50));
        funcInput.setMaximumSize(new Dimension(600, 50));
        mathFont = new Font("SansSerif", Font.PLAIN, 28);
        funcInput.setFont(mathFont);

        inputPrompt = new TextPrompt("f(z) = 0", funcInput);
        inputPrompt.changeStyle(Font.ITALIC);
        inputPrompt.changeAlpha((float) 0.6);
        inputPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        /** Press enter to solve */
        funcInput.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) { /** <- keycode for "Enter" key */
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
        fnButtons.put("sin", new CalcButton("sin", this));
        fnButtons.put("cos", new CalcButton("cos", this));
        fnButtons.put("tan", new CalcButton("tan", this));
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
        solveContainer.setPreferredSize(new Dimension(400, 80));
        solveContainer.add(solveButton);

        /** Range */

        rangeLabel = new JLabel("Range");
        rangeInput = new JTextField("10") {
            private static final long serialVersionUID = 1L;

            /** If keypress is not digit/backspace/delete/dot -> do nothing */
            @Override
            public void processKeyEvent(KeyEvent ev) {
                Character c = ev.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE
                        || c.toString().equals("."))) {
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
     * attemptFix.
     *
     * Attempts to fix missing brackets, missing "*" etc.
     *
     * TODO: This need to do a lot more than it's doing now.
     */
    void attemptFix() throws CalculatorException {
        if (this.f_z.equals("")) {
            throw new CalculatorException("Empty input");
        } else if (!f_z.contains("z")) {
            throw new CalculatorException("No variable found");
        }
        long countOBr = f_z.chars().filter(ch -> ch == '(').count();
        long countCBr = f_z.chars().filter(ch -> ch == ')').count();
        if (countOBr > countCBr) {
            while (countOBr > countCBr) {
                this.f_z += ")";
                countCBr += 1;
            }
            funcInput.setText(this.f_z);
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
            case "en":
                // TODO: localization
                break;

            case "pl":
                // TODO: localization
                break;

            case "credits":
                JOptionPane.showMessageDialog(null, "Mady by:\nPiotr Machura ID 298 183\nKacper Ledwosiński", "Credits",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case "help":
                // TODO: help window
                break;

            case "solve":
                /** Set fz equal to input field and attempt to fix it to meet the standards */
                this.f_z = funcInput.getText();
                try {
                    this.attemptFix();
                    Accuracy acc = Accuracy.MED;
                    if (accuracyMenu.getSelectedItem().equals("LOW")) {
                        acc = Accuracy.LOW;
                    } else if (accuracyMenu.getSelectedItem().equals("HIGH")) {
                        acc = Accuracy.HIGH;
                    }
                    if (!rangeAuto.isSelected()) {
                        /** Throw exception if range is not valid */
                        if (rangeInput.getText().equals("0") || rangeInput.getText().equals("")) {
                            throw new CalculatorException("Incorrect range");
                        }
                        this.range = Double.valueOf(rangeInput.getText());
                    } else {
                        this.range = 0; /** Range of 0 indicates automatic range */
                    }
                    FunctionFrame fFrame = new FunctionFrame(f_z, acc, range);
                    fFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Provided input is invalid:\n" + exc.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
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
                 *
                 * TODO: delete entire functions (ex. sin(z))
                 */
                if (caretPosition == 0) {
                    /** Edge case: caret at the beggining -> do nothing */
                    funcInput.requestFocus();
                    break;
                }

                funcInput.setText(beforeCaret.substring(0, beforeCaret.length() - 1) + afterCaret);

                /** Refocus on text field and move caret to the left */
                funcInput.requestFocus();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.setCaretPosition(caretPosition - 1);
                    }
                });
                break;
            case "auto":
                /** Prepare range to be set to 0 and disable input box */
                if (rangeAuto.isSelected()) {
                    rangeInput.setEditable(false);
                    JOptionPane.showMessageDialog(null,
                            "This looks for a rectangle so big it has at least one root inside.\nMight result in extended processing time.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
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

    /** Test */
    public static void main(final String[] args) {
        final CalculatorFrame frame = new CalculatorFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
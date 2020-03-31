package gui.src;

import javax.swing.*;

import algorithm.src.*;
import algorithm.src.Rectangle;
import parser.src.function.Complex;

import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class FunctionFrame
 */
class FunctionFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    /** Function to graph & solve */
    final String fz;

    /** Panels */
    JPanel centerPanel, bottomPanel;
    JPanel utilContainer;
    JScrollPane solutionsWrapper;

    /** Buttons */
    JButton outSpaceButton, animButton, outCloseButton;

    /** Other components */
    JTextArea solutionsDisplay;
    JLabel saveGraph, saveSolutions;
    JMenuBar upMenu;
    JMenu lanMenu, helpMenu;
    JMenuItem en, pl, help, credits;
    private static ImageIcon ICON = new ImageIcon("/cIcon.png"); // ! Nie działa

    /** Algorithm components */
    Rectangle rect, animRect;
    InputSpace space, animSpace;
    OutputSpace outSpace;
    ArrayList<Complex> solutions;
    JFrame outFrame;

    /**
     * FunctionFrame constructor.
     *
     * @param fz       the function
     * @param accuracy the accuracy level
     * @param range    the size of rectange
     */
    FunctionFrame(String fz, int accuracy, int range) {
        /** Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800, 700);
        this.setResizable(false);
        this.fz = fz;
        this.setTitle("f(z) = " + this.fz);
        this.setLocationRelativeTo(null);
        this.setIconImage(ICON.getImage());

        /** Set up algorithm components */
        space = new InputSpace(this.fz);
        animSpace = new InputSpace(this.fz);
        outSpace = new OutputSpace(this.fz);
        solutions = new ArrayList<Complex>(0);

        /** 2 rectangles - one without animation and one with animation */
        Complex A = new Complex(-range, -range);
        Complex B = new Complex(range, -range);
        Complex C = new Complex(range, range);
        Complex D = new Complex(-range, range);
        rect = new Rectangle(A, B, C, D, null, accuracy);
        animRect = new Rectangle(A, B, C, D, space, accuracy);

        /** Panels */
        centerPanel = new JPanel();
        bottomPanel = new JPanel();
        utilContainer = new JPanel();

        /** Menu bar */
        upMenu = new JMenuBar();
        upMenu.setLayout(new FlowLayout(FlowLayout.TRAILING));
        upMenu.setPreferredSize(new Dimension(700, 30));
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

        /** Output space frame */
        outFrame = new JFrame("Output space");
        outFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        outFrame.setSize(200, 250);
        outFrame.setResizable(false);
        outFrame.setLocationRelativeTo(null);

        outCloseButton = new JButton("Close");
        outCloseButton.setPreferredSize(new Dimension(80, 30));
        outCloseButton.setActionCommand("close_out");
        outCloseButton.addActionListener(this);

        outFrame.setLayout(new BorderLayout());
        outFrame.add(outSpace, BorderLayout.CENTER);
        outFrame.add(outCloseButton, BorderLayout.SOUTH);

        /** Close output space window when closing main window */
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                outFrame.dispatchEvent(new WindowEvent(outFrame, WindowEvent.WINDOW_CLOSING));
            }
        });

        /** Set up bottom panel */
        /** Scrollable solutions display */
        solutionsDisplay = new JTextArea(4, 25);
        solutionsDisplay.setEnabled(false);
        solutionsDisplay.setDisabledTextColor(Color.BLACK);
        solutionsWrapper = new JScrollPane(solutionsDisplay);
        solutionsWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        solutionsWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /** Output space button */
        outSpaceButton = new JButton("Output space");
        outSpaceButton.setPreferredSize(new Dimension(120, 30));
        outSpaceButton.setActionCommand("output");
        outSpaceButton.addActionListener(this);

        /** Animation window button */
        animButton = new JButton("Play animation");
        animButton.setPreferredSize(new Dimension(120, 30));
        animButton.setActionCommand("animation");
        animButton.addActionListener(this);

        /** Hyperlinks to save results */
        saveGraph = new JLabel("Save graph");
        saveGraph.setHorizontalAlignment(JLabel.CENTER);
        saveGraph.setForeground(Color.BLUE.darker());
        saveGraph.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        saveSolutions = new JLabel("Save solutions");
        saveSolutions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveSolutions.setForeground(Color.BLUE.darker());
        saveSolutions.setHorizontalAlignment(JLabel.CENTER);
        /** Add listeners to hyperlinks */
        saveSolutions.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: saving implementation
                System.out.println("Saving not implemented");
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }
        });
        saveGraph.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: saving implementation
                System.out.println("Saving not implemented");
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }
        });

        /** Underline hyperlinks (harder than it should be) */
        Font font = saveGraph.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        saveGraph.setFont(font.deriveFont(attributes));
        saveSolutions.setFont(font.deriveFont(attributes));

        /** Put buttons and hyperlinks in a neat container */
        utilContainer.setLayout(new GridLayout(2, 2, 0, 5));
        utilContainer.add(outSpaceButton);
        utilContainer.add(saveGraph);
        utilContainer.add(animButton);
        utilContainer.add(saveSolutions);

        bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 5));
        bottomPanel.add(solutionsWrapper);
        bottomPanel.add(utilContainer);

        /** Add main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(upMenu, BorderLayout.NORTH);
        this.add(space, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // TODO: Implement solving algorithm here
        this.addSolutionsToDisplay();
    }

    /**
     * addSolutionsToDisplay.
     *
     * Creates a neat string of text from solutions and puts it in solutionsDisplay.
     * TODO: Make it clean up the solutions (remove duplicates etc.)
     */
    private void addSolutionsToDisplay() {
        String solutionsString = "";
        if (solutions.size() == 0) {
            solutionsString = "No zeroes found within square:\n";
        } else {
            solutionsString += "Zeroes:\n";
            for (Complex complex : this.solutions) {
                solutionsString += complex + "\n";
            }
            solutionsString += "Within square:\n";
        }
        solutionsString += this.rect;
        solutionsString.trim();
        solutionsDisplay.setText(solutionsString);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String buttonID = e.getActionCommand();
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

            case "output":
                if (!outFrame.isVisible()) {
                    outFrame.setVisible(true);
                } else {
                    outFrame.setState(JFrame.NORMAL);
                    outFrame.toFront();
                    outFrame.requestFocus();
                }
                break;

            case "close_out":
                outFrame.setVisible(false);
                this.requestFocus();

            default:
                System.out.println("Unsupported operation: " + buttonID);
                break;
        }

    }

}
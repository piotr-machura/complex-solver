/**
 * Made by: Piotr Machura
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import algorithm.InputSpace;
import algorithm.OutputSpace;
import algorithm.Solver.Accuracy;
import parser.function.Complex;

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
    JButton outSpaceButton, animButton;

    /** Other components */
    JTextArea solutionsDisplay;
    JLabel saveGraph, saveSolutions;
    private static final ImageIcon ICON = new ImageIcon("cIcon.png"); // ! Nie działa

    /** Algorithm components */
    double range;
    InputSpace space, animSpace;
    OutputSpace outSpace;
    ArrayList<Complex> solutions;
    JFrame outFrame;

    /**
     * FunctionFrame constructor.
     *
     * @param fz       the function
     * @param accuracy the accuracy level
     * @param range    the size of rectangle
     */
    FunctionFrame(String fz, Accuracy acc, double range) {
        /** Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800, 700);
        this.fz = fz;
        this.setTitle("f(z) = " + this.fz);
        this.setLocationRelativeTo(null);
        this.setIconImage(ICON.getImage());

        /** Set up algorithm components */
        this.range = range;
        space = new InputSpace(this.fz);
        animSpace = new InputSpace(this.fz);
        outSpace = new OutputSpace(this.fz);

        /** Panels */
        centerPanel = new JPanel();
        bottomPanel = new JPanel();
        utilContainer = new JPanel();

        /** Output space frame */
        outFrame = new JFrame("Output space");
        outFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        outFrame.setSize(200, 250);
        outFrame.setResizable(false);
        outFrame.setLocationRelativeTo(null);
        outFrame.add(outSpace);

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
                System.out.println("Saving solutions not implemented");
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
                System.out.println("Saving the graph not implemented");
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
        this.add(space, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // TODO: Wire up solver here

        this.addSolutionsToDisplay();
    }

    /**
     * addSolutionsToDisplay.
     *
     * Creates a neat string of text from solutions and puts it in solutionsDisplay.
     */
    private void addSolutionsToDisplay() {
        String solutionsString = "";
        if (solutions == null || solutions.size() == 0) {
            solutionsString = "No zeroes found within range " + this.range + ".";
        } else {
            solutionsString += "Zeroes within range " + this.range + " :\n";
            for (Complex complex : this.solutions) {
                solutionsString += complex + "\n";
            }
        }
        solutionsString.trim();
        solutionsDisplay.setText(solutionsString);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String buttonID = e.getActionCommand();
        switch (buttonID) {
            case "output":
                if (!outFrame.isVisible()) {
                    outFrame.setVisible(true);
                } else {
                    outFrame.setState(JFrame.NORMAL);
                    outFrame.toFront();
                    outFrame.requestFocus();
                }
                break;
            case "animation":
                // TODO: Implement new GraphicSolver class
                break;

            default:
                System.out.println("Unsupported operation: " + buttonID);
                break;
        }

    }

}
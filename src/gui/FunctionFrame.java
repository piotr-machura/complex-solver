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
import javax.swing.SwingUtilities;

import algorithm.InputSpace;
import algorithm.OutputSpace;
import algorithm.Solver.Accuracy;
import algorithm.Solver;
import parser.function.Complex;

/**
 * The class FunctionFrame
 */
class FunctionFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

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
    final String f_z;
    final double range;
    InputSpace inpSpace, animSpace;
    OutputSpace outSpace;
    ArrayList<Complex> solutions;
    JFrame outFrame;
    Accuracy acc;

    /**
     * FunctionFrame constructor.
     *
     * @param f_z      the function
     * @param accuracy the accuracy level
     * @param range    the size of rectangle
     */
    FunctionFrame(String f_z, Accuracy acc, double range) {
        /** Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800, 700);
        this.f_z = f_z;
        this.setTitle("f(z) = " + this.f_z);
        this.setLocationRelativeTo(null);
        this.setIconImage(ICON.getImage());

        /** Set up algorithm components */
        this.range = range;
        solutions = null;
        inpSpace = new InputSpace(this.f_z);
        outSpace = new OutputSpace(this.f_z);
        this.acc = acc;

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
        });
        saveGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: saving implementation
                System.out.println("Saving the graph not implemented");
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
        this.add(inpSpace, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        /* Solve and showcase roots */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /** Use solver to get solutions */
                solutions = Solver.solve(range, f_z, acc);
                /** Format solutions */
                String solutionsString = "";
                if (solutions == null || solutions.size() == 0) {
                    solutionsString = "No roots found within range " + range + ".";
                } else {
                    solutionsString += "Roots within range " + range + " :\n";
                    for (Complex complex : solutions) {
                        solutionsString += complex + "\n";
                    }
                }
                solutionsString.trim();
                /** Add solutions to display and scroll to top */
                solutionsDisplay.setText(solutionsString);
                solutionsDisplay.setCaretPosition(0);
            }
        });
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
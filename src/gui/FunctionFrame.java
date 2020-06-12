package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import visual.InputSpace;
import visual.OutputSpace;
import algorithm.solver.Solver;
import algorithm.solver.SolverAccuracy;
import visual.GraphicSolver;
import algorithm.parser.function.Complex;

/**
 * The class FunctionFrame.
 *
 * JFrame with graph of function f_z and it's solutions. Displays input/otuput
 * space, allows saving graph to a .png file and soltions to .txt file.
 *
 * @Author Piotr Machura
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

    /** Algorithm components */
    final String f_z;
    final int range;
    protected static final int AUTO_RANGE = 0;
    InputSpace inpSpace;
    OutputSpace outSpace;
    ArrayList<Complex> solutions;
    JFrame outFrame;
    SolverAccuracy acc;

    /** Animation components */
    JFrame graphicSolverFrame;
    GraphicSolver graphicSolver;

    /**
     * FunctionFrame constructor.
     *
     * @param f_z      the function
     * @param accuracy the accuracy level
     * @param range    the size of rectangle
     */
    FunctionFrame(String f_z, SolverAccuracy acc, int range) {
        /** Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800, 700);
        this.f_z = f_z;
        this.setTitle("f(z) = " + this.f_z);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("cIcon.png")));

        /** Set up algorithm components */
        this.range = range;
        solutions = null;
        /** Create input space in a new thread and add it to frame */
        ExecutorService inpSpaceExec = Executors.newSingleThreadExecutor();
        inpSpaceExec.execute(new Runnable() {

            @Override
            public void run() {
                inpSpace = new InputSpace(f_z);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FunctionFrame.this.add(inpSpace, BorderLayout.CENTER);
                    }
                });
                inpSpaceExec.shutdown();
            }
        });

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
        outFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("cIcon.png")));

        /** Close output space and animation window when closing main window */
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                outFrame.dispatchEvent(new WindowEvent(outFrame, WindowEvent.WINDOW_CLOSING));
                graphicSolverFrame.dispatchEvent(new WindowEvent(graphicSolverFrame, WindowEvent.WINDOW_CLOSING));
            }
        });

        /** Set up bottom panel */
        /** Scrollable solutions display */
        solutionsDisplay = new JTextArea();
        solutionsDisplay.setEditable(false);
        solutionsDisplay.setDisabledTextColor(Color.BLACK);
        solutionsDisplay.setFont(new Font("Sans Serrif", Font.PLAIN, 16));
        solutionsDisplay.setText("Solving...");
        solutionsDisplay.setTabSize(2);
        solutionsWrapper = new JScrollPane(solutionsDisplay);
        solutionsWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        solutionsWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        solutionsWrapper.setPreferredSize(new Dimension(300, 75));

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
                try {
                    /** Choose file to save to with fileChooser */
                    JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
                    fileChooser.setDialogTitle("Save solutions");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Plain text (.txt)", "txt"));
                    if (fileChooser.showOpenDialog(FunctionFrame.this) == JFileChooser.APPROVE_OPTION) {
                        File outputFile = null;
                        if (fileChooser.getSelectedFile().toString().endsWith(".txt")) {
                            outputFile = new File(fileChooser.getSelectedFile().toString());
                        } else {
                            outputFile = new File(fileChooser.getSelectedFile() + ".txt");
                        }
                        /** osw writes text form textArea to selected file */
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFile),
                                Charset.forName("UTF-8").newEncoder());
                        osw.write("Function: " + f_z + "\n");
                        osw.write(solutionsDisplay.getText());
                        osw.close();

                        /** Notify user about succesful write */
                        JOptionPane.showMessageDialog(FunctionFrame.this,
                                "Succesfully saved solutions to:\n" + outputFile.getName(), "Succes",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(FunctionFrame.this, ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        saveGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /** Create BufferedImage from inpSpace */
                BufferedImage savedImage = new BufferedImage(inpSpace.getWidth(), inpSpace.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = savedImage.createGraphics();
                inpSpace.paintAll(g2d);
                try {
                    /** Choose file to save to with fileChooser */
                    JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
                    fileChooser.setDialogTitle("Save graph");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics (.png)", "png"));
                    if (fileChooser.showSaveDialog(FunctionFrame.this) == JFileChooser.APPROVE_OPTION) {
                        File outputFile = null;
                        if (fileChooser.getSelectedFile().toString().endsWith(".png")) {
                            outputFile = new File(fileChooser.getSelectedFile().toString());
                        } else {
                            outputFile = new File(fileChooser.getSelectedFile() + ".png");
                        }
                        /** ImageIO writes the image to outputFile */
                        ImageIO.write(savedImage, "png", outputFile);
                        /** Notify user about succesful write */
                        JOptionPane.showMessageDialog(FunctionFrame.this,
                                "Succesfully saved graph to:\n" + outputFile.getName(), "Succes",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FunctionFrame.this, ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
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
        this.add(bottomPanel, BorderLayout.SOUTH);

        /* Solve and showcase roots */
        ExecutorService solverExec = Executors.newSingleThreadExecutor();
        solverExec.execute(new Runnable() {
            @Override
            public void run() {
                /** Use solver to get solutions */
                if (range == AUTO_RANGE) {
                    solutions = Solver.solve(f_z, acc);
                } else {
                    solutions = Solver.solve(range, f_z, acc);
                }
                /** Format solutions */
                String solutionsString = "";
                if (solutions == null || solutions.size() == 0) {
                    if (range != AUTO_RANGE) {
                        solutionsString = "No roots were found within range " + range + ".";
                    } else {
                        solutionsString += "No roots were found automatically.";
                    }
                } else {
                    if (range != AUTO_RANGE) {
                        solutionsString += "Roots found in range " + range + " :\n";
                    } else {
                        solutionsString += "Roots found automatically:\n";
                    }

                    for (int i = 0; i < solutions.size(); i++) {
                        solutionsString += i + 1 + "\t|\t";
                        solutionsString += solutions.get(i) + "\n";
                    }
                }
                final String solutionsReadyToDisplay = solutionsString.trim();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        /** Add solutions to display and scroll to the top */
                        solutionsDisplay.setText(solutionsReadyToDisplay);
                        solutionsDisplay.setCaretPosition(0);
                    }
                });
                solverExec.shutdown();
            }
        });

        /** GraphicSolver definition */
        graphicSolver = new GraphicSolver(f_z, range);
        graphicSolverFrame = new JFrame("Animating input space for f(z) = " + f_z);

        /** Set GraphicSolver frame */
        graphicSolverFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        graphicSolverFrame.setSize(500, 500);
        graphicSolverFrame.setResizable(false);
        graphicSolverFrame.setLocationRelativeTo(null);
        graphicSolverFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("cIcon.png")));
        graphicSolverFrame.add(graphicSolver);

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
                if (!graphicSolverFrame.isVisible()) {
                    graphicSolver.replay();
                    graphicSolverFrame.setVisible(true);
                } else {
                    graphicSolver.replay();
                    graphicSolverFrame.setState(JFrame.NORMAL);
                    graphicSolverFrame.toFront();
                    graphicSolverFrame.requestFocus();
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unsupported operation: " + buttonID, "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

}
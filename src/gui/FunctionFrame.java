package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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
    JPanel bottomPanel;
    JPanel utilContainer;
    JScrollPane solutionsWrapper;

    /** Buttons */
    JButton outSpaceButton, animButton;

    /** Other components */
    JTextArea solutionsDisplay;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveGraph, saveSolutions, rerunCalculations;

    /** Algorithm components */
    final String f_z;
    final int range;
    static final int AUTO_RANGE = 0;
    InputSpace inpSpace;
    OutputSpace outSpace;
    ArrayList<Complex> solutions;
    SolverAccuracy acc;

    /** Animation components */
    JFrame graphicSolverFrame;
    GraphicSolver graphicSolver;

    /** Abort calculations after a time threshold has passed */
    private static final int TIMEOUT_THRESHOLD = 3000;
    private Boolean calculationsFinished = false;

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
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/main.png")));

        /** Add menu bar */
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");

        saveGraph = new JMenuItem("Save current graph");
        saveGraph.setActionCommand("saveGraph");
        saveGraph.addActionListener(this);
        saveGraph.setIcon(
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/image.png"))));

        saveSolutions = new JMenuItem("Save solutions");
        saveSolutions.setActionCommand("saveSolutions");
        saveSolutions.addActionListener(this);
        saveSolutions.setIcon(
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/file.png"))));

        rerunCalculations = new JMenuItem("Rerun calculations");
        rerunCalculations.setActionCommand("recalculate");
        rerunCalculations.addActionListener(this);
        rerunCalculations.setIcon(
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/reset.png"))));

        fileMenu.add(saveSolutions);
        fileMenu.add(saveGraph);
        fileMenu.addSeparator();
        fileMenu.add(rerunCalculations);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

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
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        utilContainer = new JPanel(new GridLayout(2, 2, 0, 5));

        /** Set up bottom panel */
        /** Scrollable solutions display */
        solutionsDisplay = new JTextArea();
        solutionsDisplay.setEditable(false);
        solutionsDisplay.setDisabledTextColor(Color.BLACK);
        solutionsDisplay.setBackground(Color.WHITE);
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

        /** Put buttons and hyperlinks in a neat container */
        utilContainer.add(outSpaceButton);
        utilContainer.add(animButton);
        JPanel invisibleEqualizer = new JPanel();
        invisibleEqualizer.setPreferredSize(new Dimension(120, 30));
        bottomPanel.add(invisibleEqualizer);
        bottomPanel.add(solutionsWrapper);
        bottomPanel.add(utilContainer);

        /** Add main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(bottomPanel, BorderLayout.SOUTH);

        /* Solve and showcase roots */
        this.calculate();

        /** GraphicSolver definition */
        graphicSolver = new GraphicSolver(f_z, range);
        graphicSolverFrame = new JFrame("Animating input space for f(z) = " + f_z);
        graphicSolverFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                graphicSolver.graphicExec.shutdownNow();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        /** Set GraphicSolver frame */
        graphicSolverFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        graphicSolverFrame.setSize(500, 500);
        graphicSolverFrame.setResizable(false);
        graphicSolverFrame.setLocationRelativeTo(null);
        graphicSolverFrame
                .setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/main.png")));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                graphicSolverFrame.dispatchEvent(new WindowEvent(FunctionFrame.this, WindowEvent.WINDOW_CLOSING));
                graphicSolver.outFrame.dispatchEvent(new WindowEvent(FunctionFrame.this, WindowEvent.WINDOW_CLOSING));
                super.windowClosing(e);
            }
        });
        graphicSolverFrame.add(graphicSolver);

    }

    private void calculate() {
        ExecutorService solverExec = Executors.newSingleThreadExecutor();
        solverExec.execute(new Runnable() {
            @Override
            public void run() {
                Timer timeoutTimer = new Timer(TIMEOUT_THRESHOLD, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!calculationsFinished) {
                            solutionsDisplay
                                    .setText("The solver timed out after " + TIMEOUT_THRESHOLD / 1000 + " seconds.");
                        }
                        solverExec.shutdownNow();

                    }
                });
                timeoutTimer.start();
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
                calculationsFinished = true;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String buttonID = e.getActionCommand();
        switch (buttonID) {
            case "output":
                if (!graphicSolver.outFrame.isVisible()) {
                    graphicSolver.outFrame.setVisible(true);
                } else {
                    graphicSolver.outFrame.setState(JFrame.NORMAL);
                    graphicSolver.outFrame.toFront();
                    graphicSolver.outFrame.requestFocus();
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
            case "saveSolutions":
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
                break;
            case "saveGraph":
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
                break;
            case "recalculate":
                this.solutionsDisplay.setText("Recalculating...");
                this.calculate();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unsupported operation: " + buttonID, "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

}
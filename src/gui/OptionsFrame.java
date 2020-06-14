package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import algorithm.solver.Solver;
import algorithm.solver.SolverDefaults;

/**
 * The singleton class OptionsFrame.
 *
 * Creates and modifies the .solverrc file, allowing for changes in the solver
 * behaviour.
 *
 * @Author Piotr Machura
 */
public class OptionsFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The singleton instance */
    private static OptionsFrame instance = new OptionsFrame();

    /**
     * getInstance.
     *
     * @return the singleton instance of OptionsFrame
     */
    public static OptionsFrame getInstance() {
        return instance;
    }

    /** GUI elements */
    JPanel optionsPanel;
    JTextArea warningArea;
    JTextArea[] optionLabels;
    JTextField[] optionFields;
    JButton applyButton, restoreButton;
    public static final File solverrc = new File(System.getProperty("user.dir") + "\\.solverrc");

    private OptionsFrame() {
        super("Options");
        this.setSize(570, 320);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 5));
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/main.png")));

        /**
         * The panel displaying the warning
         */
        warningArea = new JTextArea();
        warningArea.setPreferredSize(new Dimension(540, 55));
        warningArea.setText(
                "WARNING: Changing any of theese variables might result in the solving algorithm breaking. Make sure you know what you are doing by reading the \"Advanced settings\" help document.");
        warningArea.setEditable(false);
        warningArea.setBackground(Color.WHITE);
        warningArea.setLineWrap(true);
        warningArea.setMargin(new Insets(15, 10, 10, 10));
        warningArea.setFont(new Font("SansSerif", Font.BOLD, 12));
        warningArea.setWrapStyleWord(true);
        /**
         * Options panel
         */
        optionsPanel = new JPanel(new GridLayout(4, 2, 0, 0));
        optionLabels = new JTextArea[7];
        optionLabels[0] = new JTextArea("Auto range start");
        optionLabels[1] = new JTextArea("Auto range increment");
        optionLabels[2] = new JTextArea("Auto range max");
        optionLabels[3] = new JTextArea("Maximum legal delta phi");
        optionLabels[4] = new JTextArea("Steps per sidelength");
        optionLabels[5] = new JTextArea("Minimum legal winding number");
        optionLabels[6] = new JTextArea("Maximum legal abs of root");

        for (JTextArea label : optionLabels) {
            label.setLineWrap(true);
            label.setBackground(Color.WHITE);
            label.setLineWrap(true);
            label.setWrapStyleWord(true);
            label.setEditable(false);
            label.setPreferredSize(new Dimension(110, 40));
        }

        optionFields = new JTextField[7];
        for (int i = 0; i < optionFields.length; i++) {
            if (i == 3 || i == 5 || i == 6) {
                /** Fields 3, 5 and 6 (delta phi, winding number, abs of root) are float-only */
                optionFields[i] = new FloatOnlyJTextField(8);
            } else {
                /** Rest are int-only */
                optionFields[i] = new IntOnlyJTextField(8);
            }
            optionFields[i].setPreferredSize(new Dimension(40, 30));
            optionFields[i].setFont(new Font("SansSerif", Font.PLAIN, 16));
            optionFields[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(final KeyEvent e) {
                    final int ENTER_KEYCODE = 10;
                    if (e.getKeyCode() == ENTER_KEYCODE) {
                        applyButton.doClick();
                    }
                }
            });

        }

        /** Press enter to apply */
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int ENTER_KEYCODE = 10;
                if (e.getKeyCode() == ENTER_KEYCODE) {
                    applyButton.doClick();
                }
            }
        });

        for (int i = 0; i < optionFields.length; i++) {
            JPanel labelWrapperPanel = new JPanel(new FlowLayout());
            JPanel fieldWrapperPanel = new JPanel(new FlowLayout());
            fieldWrapperPanel.setAlignmentY((float) fieldWrapperPanel.getSize().getHeight() / 2);
            labelWrapperPanel.add(optionLabels[i]);
            fieldWrapperPanel.add(optionFields[i]);
            optionsPanel.add(labelWrapperPanel);
            optionsPanel.add(fieldWrapperPanel);
        }
        /**
         * Buttons panel
         */
        applyButton = new JButton("Apply");
        applyButton.setPreferredSize(new Dimension(120, 40));
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);
        JPanel applyButtonWrapper = new JPanel(new FlowLayout());
        applyButtonWrapper.add(applyButton);

        restoreButton = new JButton("Restore defaults");
        restoreButton.setPreferredSize(new Dimension(120, 40));
        restoreButton.setActionCommand("restore");
        restoreButton.addActionListener(this);
        JPanel restoreButtonWrapper = new JPanel(new FlowLayout());
        restoreButtonWrapper.add(restoreButton);

        optionsPanel.add(applyButtonWrapper);
        optionsPanel.add(restoreButtonWrapper);

        this.add(warningArea, BorderLayout.NORTH);
        this.add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * setVisible.
     *
     * When the options window is invoked it reads the current config file and sets
     * the optionFields accordingly.
     */
    @Override
    public void setVisible(boolean b) {
        /** Get config file from cwd */
        File solverrc = new File(System.getProperty("user.dir") + "\\.solverrc");
        try {
            /** Read the file to fileContents with isr */
            InputStreamReader isr = new InputStreamReader(new FileInputStream(solverrc),
                    Charset.forName("UTF-8").newDecoder());
            int data = isr.read();
            String fileContents = "";
            while (data != -1) {
                fileContents += (char) data;
                data = isr.read();
            }
            isr.close();
            fileContents = fileContents.trim();
            String[] args = fileContents.split(" ");
            if (args.length != 7) {
                throw new NumberFormatException("Incorrect argument count");
            }
            for (int i = 0; i < args.length; i++) {
                optionFields[i].setText(args[i]);
            }
        } catch (Exception e) {
            /**
             * Something is wrong with the .solverrc file (or it doesn't exist) - load
             * defaults
             */
            restoreDefaults();
        }
        super.setVisible(b);
    }

    private void restoreDefaults() {
        optionFields[0].setText("" + SolverDefaults.AUTO_RANGE_START);
        optionFields[1].setText("" + SolverDefaults.AUTO_RANGE_INCREMENT);
        optionFields[2].setText("" + SolverDefaults.AUTO_RANGE_MAX);
        optionFields[3].setText("" + SolverDefaults.MAX_LEGAL_DELTAPHI_RATIO);
        optionFields[4].setText("" + SolverDefaults.STEPS_PER_SIDELENGTH);
        optionFields[5].setText("" + SolverDefaults.MIN_LEGAL_WINDING_NUMBER_RATIO);
        optionFields[6].setText("" + SolverDefaults.MAX_LEGAL_ABS_OF_ROOT);
        Solver.restoreDefaultConfig();
        try {
            solverrc.delete();
        } catch (Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(OptionsFrame.this, "An error has occured:\n" + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

    }

    private void applyChangesToConfig(File solverrc) throws IOException {
        solverrc.createNewFile();
        /** osw writes text form textArea to selected file */
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(solverrc),
                Charset.forName("UTF-8").newEncoder());
        for (int i = 0; i < optionFields.length; i++) {
            osw.write(optionFields[i].getText());
            osw.write(" ");
        }
        osw.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonID = e.getActionCommand();
        switch (buttonID) {
            case "apply":
                try {
                    applyChangesToConfig(solverrc);
                    Solver.readConfig(solverrc);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(OptionsFrame.this, "Succesfully applied changes", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                } catch (Exception exc) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(OptionsFrame.this,
                                    "An error has occured:\n" + exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
                this.setVisible(false);
                break;
            case "restore":
                this.restoreDefaults();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(OptionsFrame.this, "Defaults sucessfuly restored", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unsupported operation: " + buttonID, "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

}
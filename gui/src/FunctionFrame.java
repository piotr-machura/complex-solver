package gui.src;

import javax.swing.*;

import algorithm.src.*;
import algorithm.src.Rectangle;
import parser.src.function.Complex;

import java.awt.*;
import java.awt.event.*;

class FunctionFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    /** Function to graph & solve */
    final String fz;

    /** Panels */
    JPanel centerPanel;
    Rectangle rect, animRect;

    InputSpace space, spaceAnimation;
    OutputSpace outSpace;
    private static ImageIcon ICON = new ImageIcon("/cIcon.png"); // ! Nie działa

    FunctionFrame(String fz, int accuracy, int sizeOfRect) {
        /** Set up rectangles - one without animation and one with animation */
        Complex A = new Complex(-sizeOfRect, -sizeOfRect);
        Complex B = new Complex(sizeOfRect, -sizeOfRect);
        Complex C = new Complex(sizeOfRect, sizeOfRect);
        Complex D = new Complex(-sizeOfRect, sizeOfRect);
        rect = new Rectangle(A, B, C, D, null, accuracy);
        animRect = new Rectangle(A, B, C, D, space, accuracy);
        /** Basic parameters */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setResizable(false);
        this.fz = fz;
        this.setTitle("Complex Solver: " + this.fz);
        this.setIconImage(ICON.getImage());

        /** Panels */
        centerPanel = new JPanel();

        /** Main input space */
        space = new InputSpace(this.fz);
        space.setPreferredSize(new Dimension(500, 400));
        centerPanel.add(space);
        centerPanel.setPreferredSize(new Dimension(500, 400));

        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
    }

}
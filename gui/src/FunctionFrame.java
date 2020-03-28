package gui.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import parser.src.function.*;
import parser.src.main.*;
import parser.src.util.*;

class FunctionFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    final String fz;

    FunctionFrame(String fz) {
        /*
         * Basic parameters
         */
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 500);
        this.setResizable(false);
        this.fz = fz;
        this.setTitle("Complex Solver: " + this.fz);
    }

}
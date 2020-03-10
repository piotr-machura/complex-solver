package algorithm.src;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        Function f = new Function();

        InputSpace space = new InputSpace(f);
        OutputSpace output = new OutputSpace(f);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.add(space);
        frame.setVisible(true);

        JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.setSize(300, 300);
        frame2.add(output);
        frame2.setVisible(true);

        Complex A = new Complex(-2, -2);
        Complex B = new Complex(2, -2);
        Complex C = new Complex(2, 2);
        Complex D = new Complex(-2, 2);

        Rectangle r1 = new Rectangle(A, B, C, D, space, output);
        
        r1.solveInside(f);

        System.out.println(f.stringSolutions());

    }
}
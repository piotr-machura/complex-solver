/**
 * Made by: Piotr Machura, Kacper Ledwosiński
 */
package algorithm.src;

import java.util.ArrayList;
import javax.swing.*;

import parser.src.exception.CalculatorException;
import parser.src.function.*;
import parser.src.main.*;
import parser.src.util.*;

/*
 * The class Rectangle
 *
 * D-----CD_mid-----C
 * |                |
 * AD_mid MIDDLE BC_mid
 * |                |
 * A-----AB_mid-----B
 *
 */

public class Rectangle {
    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    int accuracyLevel;
    InputSpace space;

    /**
     * Rectangle constructor.
     *
     * Constructs a rectangle using points A, B, C, D (as shown above)
     *
     * @param a             the down-left point
     * @param b             the down-right point
     * @param c             the up-right point
     * @param d             the up-left point
     * @param space         the input space to draw in
     * @param accuracyLevel the accuracy level
     */
    public Rectangle(Complex a, Complex b, Complex c, Complex d, InputSpace space, int accuracyLevel) {

        A = a;
        B = b;
        C = c;
        D = d;
        this.space = space;
        this.accuracyLevel = accuracyLevel;

        /** Calculating mid-points based on given points */
        AB_mid = new Complex((B.getRe() + A.getRe()) / 2, A.getIm());
        BC_mid = new Complex(B.getRe(), (C.getIm() + B.getIm()) / 2);
        CD_mid = new Complex((C.getRe() + D.getRe()) / 2, C.getIm());
        AD_mid = new Complex(D.getRe(), (D.getIm() + A.getIm()) / 2);
        MIDDLE = new Complex((BC_mid.getRe() + AD_mid.getRe()) / 2, (CD_mid.getIm() + AB_mid.getIm()) / 2);

        /** Calculating area of rectangle */
        area = (B.getRe() - A.getRe()) * (C.getIm() - B.getIm());
    }

    /** toString() */
    public String toString() {
        String rectString = "";
        rectString += "D: " + D.toString() + "   ";
        rectString += "C: " + C.toString() + "\n";
        rectString += "A: " + A.toString() + "   ";
        rectString += "B: " + B.toString() + "\n";
        rectString += "area: " + area;
        return rectString;
    }

    /**
     * checkInside.
     *
     * * If the phase big -> small (ex. 1.9PI -> 0.1 PI, deltaPhi == -1.8PI) this
     * means that Re+ axis was crossed POSITIVELY, hence add 2PI to deltaPhi (-1.8PI
     * +2PI = 0.2PI, correct phase change).
     *
     * If the phase went small -> big (ex. 0.1PI -> 1.9 PI, deltaPhi == 1.8PI) this
     * means that Re+ axis was crossed NEGATIVELY, hence substract 2PI from deltaPhi
     * (1.8PI - 2PI = -0.2PI, correct phase change)
     *
     * ! Czasem jest błąd: phase undefined for point NaN + NaN i.
     *
     * Możliwe, że to dlatego że czasem ma 0^0 i to jest NaN
     *
     * @return winding number close or greater than 1
     */
    public Boolean checkInside(String f) {

        /** Tick of "integration" - 1000 points checked per side length */
        double d = 0.001 * Math.sqrt(this.area);
        double windingNumber = 0;

        /** Starting number: A */
        double x = A.getRe();
        double y = A.getIm();

        /** Path A->B (going right) */
        while (x < B.getRe()) {
            try {
                /** For small rectangles there is no need to draw them */
                if (this.area > 0.001 && space != null) {
                    space.addPoint(new Complex(x, y));
                }
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.phase(prev);
                x += d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.phase(now);
                double deltaPhi = nowPhi - prevPhi;
                if (deltaPhi > 1.9 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.9 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
            } catch (CalculatorException e) {
                // e.printStackTrace();
                if (x + d < B.getRe()) {
                    x += d;
                } else {
                    break;
                }
            }

        }

        /** Path B->C (going up) */
        while (y < C.getIm()) {
            try {
                if (this.area > 0.001 && space != null) {
                    space.addPoint(new Complex(x, y));
                }
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.phase(prev);
                y += d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.phase(now);
                double deltaPhi = nowPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
            } catch (CalculatorException e) {
                // e.printStackTrace();
                if (y + d < C.getIm()) {
                    y += d;
                } else {
                    break;
                }
            }
        }

        /** Path C->D (going left) */
        while (x > D.getRe()) {
            try {
                if (this.area > 0.001 && space != null) {
                    space.addPoint(new Complex(x, y));
                }
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.phase(prev);
                x -= d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.phase(now);
                double deltaPhi = nowPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
            } catch (CalculatorException e) {
                // e.printStackTrace();
                if (x - d > D.getRe()) {
                    x -= d;
                } else {
                    break;
                }
            }
        }

        /** Path D->A (going down) */
        while (y > A.getIm()) {
            try {
                if (this.area > 0.001 && space != null) {
                    space.addPoint(new Complex(x, y));
                }
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.phase(prev);
                y -= d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.phase(now);
                double deltaPhi = nowPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
            } catch (Exception e) {
                // e.printStackTrace();
                if (y - d > A.getIm()) {
                    y -= d;
                } else {
                    break;
                }
            }
        }
        /** Total number of revolutions = (total phase change) / 2 PI */
        windingNumber = windingNumber / (2 * Math.PI);

        /** Checks if winding number is non-zero */
        final double epsilon = 0.001;
        return Math.abs(windingNumber) > 1 - epsilon;
    }

    /**
     * getChildren.
     *
     * Splits the rectangle into 4 children, enumerated starting bottom left
     * clockwise
     *
     * @return chlidren the array with children
     */
    Rectangle[] getChildren() {

        Rectangle[] children = new Rectangle[4];
        children[0] = new Rectangle(A, AB_mid, MIDDLE, AD_mid, space, accuracyLevel);
        children[1] = new Rectangle(AB_mid, B, BC_mid, MIDDLE, space, accuracyLevel);
        children[2] = new Rectangle(MIDDLE, BC_mid, C, CD_mid, space, accuracyLevel);
        children[3] = new Rectangle(AD_mid, MIDDLE, CD_mid, D, space, accuracyLevel);
        return children;
    }

    /**
     * solveInside.
     *
     * Recursively checks rectangle's winding number, splitting it into 4 children
     * if it's big and viable and discarding it if it's not viable. If it's small
     * and viable, adds it's middle to f's solution list
     *
     * @param fz        the function
     * @param solutions the list to put solutions in
     */
    public void solveInside(String fz, ArrayList<Complex> solutions) {
        if (this.checkInside(fz)) {
            if (this.area <= Math.pow(10, -5 * this.accuracyLevel)) {
                solutions.add(this.MIDDLE);
            } else {
                Rectangle[] children = this.getChildren();
                for (Rectangle child : children) {
                    child.solveInside(fz, solutions);
                }
            }
        }
    }

    public static void main(String[] args) {
        String f = "cos(z)";
        System.out.println(f);
        InputSpace space = new InputSpace(f);
        // OutputSpace output = new OutputSpace(f);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(space);
        frame.setVisible(true);

        // JFrame frame2 = new JFrame();
        // frame2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // frame2.setSize(300, 300);
        // frame2.add(output);
        // frame2.setVisible(true);

        Complex A = new Complex(-5, -5);
        Complex B = new Complex(5, -5);
        Complex C = new Complex(5, 5);
        Complex D = new Complex(-5, 5);
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        Rectangle s1 = new Rectangle(A, B, C, D, null, 4);
        System.out.println("Solving for: " + f + "= 0 in rectangle: \n" + s1);
        s1.solveInside(f, solutions);
        System.out.println(solutions);
    }

}

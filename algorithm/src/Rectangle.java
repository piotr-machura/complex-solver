package algorithm.src;

import java.util.ArrayList;

import parser.src.function.*;
import parser.src.main.*;
import parser.src.util.*;

/*
        Rectangle
    D-----CD_mid-----C
    |                |
  AD_mid   MIDDLE   BC_mid
    |                |
    A-----AB_mid-----B
*/

public class Rectangle {
    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    InputSpace space;
    OutputSpace output;

    /*
     * Rectangle.
     *
     * Constructs a rectangle using points A, B, C, D (as shown above) bound to
     * provided input and output spaces.
     */
    public Rectangle(Complex a, Complex b, Complex c, Complex d, InputSpace space, OutputSpace output) {

        A = a;
        B = b;
        C = c;
        D = d;
        this.space = space;
        this.output = output;

        /* Calculating mid-points based on given points */
        AB_mid = new Complex((B.getRe() + A.getRe()) / 2, A.getIm());
        BC_mid = new Complex(B.getRe(), (C.getIm() + B.getIm()) / 2);
        CD_mid = new Complex((C.getRe() + D.getRe()) / 2, C.getIm());
        AD_mid = new Complex(D.getRe(), (D.getIm() + A.getIm()) / 2);
        MIDDLE = new Complex((BC_mid.getRe() + AD_mid.getRe()) / 2, (CD_mid.getIm() + AB_mid.getIm()) / 2);

        /* Calculating area of rectangle */
        area = (B.getRe() - A.getRe()) * (C.getIm() - B.getIm());
    }

    /* toString() */
    public String toString() {
        String rectString = "";
        rectString += "D: " + D.toString() + "   ";
        rectString += "C: " + C.toString() + "\n";
        rectString += "A: " + A.toString() + "   ";
        rectString += "B: " + B.toString() + "\n";
        rectString += "area: " + area;
        return rectString;
    }

    // ! To jeszcze nie działa
    /*
     * Arg (-2+0i) = pi, ale Arg(-2-0.01i) = -pi+odrobine, czyli zmiana jest prawie
     * 2pi. Problem jest przy "mijaniu" osi ujemnych Re, bo wtedy jest przeskok o
     * 2pi w fazie. Dopisałem do klasy Complex funkcję zwracającą fazę [0, 2pi] +
     * 2kpi gdzie k będzie obecnym winding number, trzeba ją tylko zaimplementować.
     * Trzeba też nauczyć tą rekurencję kiedy ma się zabić bo potrafi dodać 30
     * takich samych miejsc zerowych.
     */

    /*
     * checkInside.
     *
     * Checks if winding number of a given rectangle is NOT close to zero.
     */
    public Boolean checkInside(String f) {

        /* Tick of "integration" - 1/10th of side length */
        double d = Math.sqrt(this.area) / 10;
        double windingNumber = 0;
        int k = 0;

        /* Starting number: A */
        double x = A.getRe();
        double y = A.getIm();

        /* Path A->B (going right) */
        while (x < B.getRe()) {
            try {
                space.addPoint(new Complex(x, y));
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.kPhase(prev, k);
                x += d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.kPhase(now, k);
                windingNumber += nowPhi - prevPhi;
            } catch (Exception e) {
                x += d;
            }

        }

        /* Path B->C (going up) */
        while (y < C.getIm()) {
            try {
                space.addPoint(new Complex(x, y));
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.kPhase(prev, k);
                y += d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.kPhase(now, k);
                windingNumber += nowPhi - prevPhi;
            } catch (Exception e) {
                y += d;
            }
        }

        /* Path C->D (going left) */
        while (x > D.getRe()) {
            try {
                space.addPoint(new Complex(x, y));
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.kPhase(prev, k);
                x -= d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.kPhase(now, k);
                windingNumber += nowPhi - prevPhi;
            } catch (Exception e) {
                x -= d;
            }
        }

        /* Path D->A (going down) */
        while (y > A.getIm()) {
            try {
                space.addPoint(new Complex(x, y));
                Complex prev = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double prevPhi = Complex.kPhase(prev, k);
                x -= d;
                Complex now = Parser.eval(f, new Variable("z", new Complex(x, y))).getComplexValue();
                double nowPhi = Complex.kPhase(now, k);
                windingNumber += nowPhi - prevPhi;
            } catch (Exception e) {
                x -= d;
            }
        }

        /* Total number of revolutions = (total phase change) / 2 PI */
        windingNumber = windingNumber / (2 * Math.PI);
        // System.out.println("Winding number: " + windingNumber + "\n\n");

        /* Checks if winding number sufficiently bigger than zero */
        final double epsilon = 0.1;
        return Math.abs(windingNumber) > epsilon;
    }

    /*
     * getChildren.
     *
     * Splits the rectangle into 4 children, enumerated starting bottom left
     * clockwise
     */
    Rectangle[] getChildren() {

        Rectangle[] children = new Rectangle[4];
        children[0] = new Rectangle(A, AB_mid, MIDDLE, AD_mid, space, output);
        children[1] = new Rectangle(AB_mid, B, BC_mid, MIDDLE, space, output);
        children[2] = new Rectangle(MIDDLE, BC_mid, C, CD_mid, space, output);
        children[3] = new Rectangle(AD_mid, MIDDLE, CD_mid, D, space, output);
        return children;
    }

    /*
     * solveInside.
     *
     * Recursively checks rectangle's winding number, splitting it into 4 children
     * if it's big and viable and discarding it if it's not viable. If it's small
     * and viable, adds it's middle to f's solution list
     */
    public void solveInside(String f, ArrayList<Complex> solutions) {

        System.out.println("Checking rectangle: \n" + toString() + "\n");
        if (this.checkInside(f)) {

            if (this.area <= 0.001) {
                solutions.add(this.MIDDLE);
            } else {
                Rectangle[] children = this.getChildren();
                for (Rectangle child : children) {
                    child.solveInside(f, solutions);
                }
            }
        }
    }

    public static void main(String[] args) {
        String f = "e^z-1";
        System.out.println(f);
        InputSpace space = new InputSpace(f);
        OutputSpace output = new OutputSpace(f);
        Complex A = new Complex(-2, -2);
        Complex B = new Complex(2, -2);
        Complex C = new Complex(2, 2);
        Complex D = new Complex(-2, 2);
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        Rectangle s1 = new Rectangle(A, B, C, D, space, output);
        System.out.println("Started solving...\n");
        s1.solveInside(f, solutions);
        System.out.println(solutions);
    }

}

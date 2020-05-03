/**
 * Made by: Piotr Machura
 */
package algorithm;

import java.util.ArrayList;
import java.util.Collections;

import parser.exception.CalculatorException;
import parser.function.Complex;
import parser.main.Parser;
import parser.util.Variable;

/*
 *        Square diagram:
 *    D-------CD_mid-------C
 *    |                    |
 *  AD_mid    MIDDLE    BC_mid
 *    |                   |
 *    A-------AB_mid------B
 */

/**
 * The class Solver.
 *
 * All methods, including contructor, are considered private/protected. In order
 * to utilize this class use the static solve(double, String, Accuracy)
 * function.
 */
public class Solver {

    /**
     * Accuracy enum
     *
     * @param LOW  ~4 significant digits
     * @param MED  ~5 significant digits
     * @param HIGH ~6 significant digits
     */
    public enum Accuracy {
        LOW, MED, HIGH
    }

    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    final Accuracy accuracyLevel;

    /**
     * solve.
     *
     * Static method returning simplified ArrayList of solutions to f_z in a given
     * range, sorted in ascending order according to method described in
     * Complex.compareTo() function.
     *
     * TODO: create a version with range 0 (an "auto sized" solver).
     *
     * @param range         half of the sidelength of rectangle to look in
     * @param f_z           the function to solve
     * @param accuracyLevel the desired accuracyLevel
     *
     * @return solutions the formatted and sorted ArrayList of solutions
     */
    public static ArrayList<Complex> solve(final double range, final String f_z, final Accuracy accuracyLevel) {
        /**
         * Get solutions inside rectangle of side length 2*range using recursive
         * solveInside function.
         */
        Complex A = new Complex(-range, -range);
        Complex B = new Complex(range, -range);
        Complex C = new Complex(range, range);
        Complex D = new Complex(-range, range);
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Solver(A, B, C, D, accuracyLevel).solveInside(f_z, solutions);

        /**
         * Round all soultions according to acc, delete duplicates and sort list
         */
        double rd;
        if (accuracyLevel == Accuracy.LOW) {
            rd = 1000d;
        } else if (accuracyLevel == Accuracy.MED) {
            rd = 10000d;
        } else { /* acc == AcLevel.HIGH */
            rd = 100000d;
        }
        /** Round decimals according to accuracyLevel */
        for (int i = 0; i < solutions.size(); i++) {
            double reI = (double) Math.round(solutions.get(i).getRe() * rd) / rd;
            double imI = (double) Math.round(solutions.get(i).getIm() * rd) / rd;
            solutions.set(i, new Complex(reI, imI));
        }

        /** Remove duplicates */
        for (int i = 0; i < solutions.size() - 1; i++) {
            /** Take current Re & Im */
            double reI = solutions.get(i).getRe();
            double imI = solutions.get(i).getIm();
            for (int j = i + 1; j < solutions.size(); j++) {
                /** Compare with all other solutions and delete them if duplicated */
                double reJ = solutions.get(j).getRe();
                double imJ = solutions.get(j).getIm();
                if (Math.abs(reJ - reI) <= 10 / rd && Math.abs(imJ - imI) <= 10 / rd) {
                    solutions.remove(j);
                }

            }
        }
        /** Sort and return solutions */
        Collections.sort(solutions);
        return solutions;
    }

    /**
     * Square constructor.
     *
     * Constructs a square using points A, B, C, D as shown in Solver.java header.
     *
     * @param a        the bottom-left point A
     * @param b        the bottom-right point B
     * @param c        the top-right point C
     * @param d        the top-left point D
     * @param Accuracy the desired accuracy level
     */
    protected Solver(Complex a, Complex b, Complex c, Complex d, final Accuracy accuracyLevel) {

        A = a;
        B = b;
        C = c;
        D = d;
        this.accuracyLevel = accuracyLevel;

        /** Calculate mid-points based on given points */
        AB_mid = new Complex((B.getRe() + A.getRe()) / 2, A.getIm());
        BC_mid = new Complex(B.getRe(), (C.getIm() + B.getIm()) / 2);
        CD_mid = new Complex((C.getRe() + D.getRe()) / 2, C.getIm());
        AD_mid = new Complex(D.getRe(), (D.getIm() + A.getIm()) / 2);
        MIDDLE = new Complex((BC_mid.getRe() + AD_mid.getRe()) / 2, (CD_mid.getIm() + AB_mid.getIm()) / 2);

        /** Calculate area of the square */
        area = (B.getRe() - A.getRe()) * (C.getIm() - B.getIm());
    }

    /**
     * checkWindingNumber.
     *
     * Check winding number in relation to function f_z.
     *
     * @return Bool: winding number close or greater than 1
     */
    private Boolean checkWindingNumber(final String f_z) {

        /** Step of "integration" - 200 steps per side length */
        final double step = 0.005 * Math.sqrt(this.area);
        double windingNumber = 0;

        /** Starting point: A */
        double x = A.getRe();
        double y = A.getIm();
        /* Phase prior to step (to be remembered) */
        double prevPhi = 0;
        try {
            prevPhi = Complex.phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
        } catch (Exception e) {
            /** Strating in zero - might as well consider phase to be zero. */
        }

        /** Path A->B (going right) */
        while (x < B.getRe()) {
            try {
                /** Go a step forward */
                x += step;
                /** Calculate phase after taking a step */
                double nextPhi = Complex
                        .phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
                /** Calculate phase change */
                double deltaPhi = nextPhi - prevPhi;
                /**
                 * If the phase went from small -> big (ex. 0.1PI -> 1.9 PI, deltaPhi == 1.8PI)
                 * this means that Re+ axis was crossed NEGATIVELY, hence substract 2PI from
                 * deltaPhi (1.8PI - 2PI = -0.2PI, correct phase change)
                 */
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                }
                /**
                 * If the phase went from big -> small (ex. 1.9PI -> 0.1 PI, deltaPhi == -1.8PI)
                 * this means that Re+ axis was crossed POSITIVELY, hence add 2PI to deltaPhi
                 * (-1.8PI +2PI = 0.2PI, correct phase change).
                 */
                else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
                prevPhi = nextPhi;
            } catch (CalculatorException e) {
                /**
                 * This means a zero was encountered and phase cannot be calculated. If moving a
                 * step forward won't go outisde boundary then move, if it will go outisde
                 * boundary (which means you are at a corner) then proceed to checking next
                 * side.
                 *
                 * TODO: Make it "circle" around zero insted of just passing through it.
                 *
                 */
                if (x + step < B.getRe()) {
                    x += step;
                } else {
                    break;
                }
            }

        }

        /** Path B->C (going up) */
        while (y < C.getIm()) {
            try {
                y += step;
                double nextPhi = Complex
                        .phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
                double deltaPhi = nextPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
                prevPhi = nextPhi;
            } catch (CalculatorException e) {
                if (y + step < C.getIm()) {
                    y += step;
                } else {
                    break;
                }
            }
        }

        /** Path C->D (going left) */
        while (x > D.getRe()) {
            try {
                x -= step;
                double nextPhi = Complex
                        .phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
                double deltaPhi = nextPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
                prevPhi = nextPhi;
            } catch (CalculatorException e) {
                if (x - step > D.getRe()) {
                    x -= step;
                } else {
                    break;
                }
            }
        }

        /** Path D->A (going down) */
        while (y > A.getIm()) {
            try {
                y -= step;
                double nextPhi = Complex
                        .phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
                double deltaPhi = nextPhi - prevPhi;
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                } else if (deltaPhi < -1.8 * Math.PI) {
                    deltaPhi += 2 * Math.PI;
                }
                windingNumber += deltaPhi;
                prevPhi = nextPhi;
            } catch (Exception e) {
                if (y - step > A.getIm()) {
                    y -= step;
                } else {
                    break;
                }
            }
        }
        /** Total number of revolutions = (total phase change) / 2 PI */
        windingNumber = windingNumber / (2 * Math.PI);

        /** Checks if winding number is non-zero */
        return Math.abs(windingNumber) > 0.95;
    }

    /**
     * solveInside.
     *
     * Recursively check square's winding number, splitting it into 4 children if
     * it's big and viable and discarding it if it's not viable. If it's small and
     * viable, add its middle to solutions.
     *
     * @param f_z       the function to solve for
     * @param solutions the arraylist to put solutions in
     */
    private void solveInside(final String f_z, ArrayList<Complex> solutions) {
        if (this.checkWindingNumber(f_z)) {
            if (this.area <= Math.pow(10, -2 * (this.accuracyLevel.ordinal() + 3))) {
                solutions.add(this.MIDDLE);
            } else {
                Solver[] children = new Solver[4];
                children[0] = new Solver(A, AB_mid, MIDDLE, AD_mid, accuracyLevel);
                children[1] = new Solver(AB_mid, B, BC_mid, MIDDLE, accuracyLevel);
                children[2] = new Solver(MIDDLE, BC_mid, C, CD_mid, accuracyLevel);
                children[3] = new Solver(AD_mid, MIDDLE, CD_mid, D, accuracyLevel);
                for (Solver child : children) {
                    child.solveInside(f_z, solutions);
                }
            }
        }
    }
}

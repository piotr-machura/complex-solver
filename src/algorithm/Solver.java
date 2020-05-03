/**
 * Made by: Piotr Machura, Kacper Ledwosiński
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
 * The class Solver
 */
public class Solver {

    public enum Accuracy {
        /**
         * Accuracy enum
         *
         * @param LOW  ~4 significant digits
         * @param MED  ~5 significant digits
         * @param HIGH ~6 significant digits
         */
        LOW, MED, HIGH
    }

    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    Accuracy acc;

    /**
     * Rectangle constructor.
     *
     * Constructs a square using points A, B, C, D (as shown above)
     *
     * ! This is always a square, NOT a rectangle
     *
     * @param a        the bottom-left point A
     * @param b        the bottom-right point B
     * @param c        the top-right point C
     * @param d        the top-left point D
     * @param Accuracy the desired accuracy level
     */
    public Solver(Complex a, Complex b, Complex c, Complex d, Accuracy acc) {

        A = a;
        B = b;
        C = c;
        D = d;
        this.acc = acc;

        /** Calculating mid-points based on given points */
        AB_mid = new Complex((B.getRe() + A.getRe()) / 2, A.getIm());
        BC_mid = new Complex(B.getRe(), (C.getIm() + B.getIm()) / 2);
        CD_mid = new Complex((C.getRe() + D.getRe()) / 2, C.getIm());
        AD_mid = new Complex(D.getRe(), (D.getIm() + A.getIm()) / 2);
        MIDDLE = new Complex((BC_mid.getRe() + AD_mid.getRe()) / 2, (CD_mid.getIm() + AB_mid.getIm()) / 2);

        /** Calculating area of the square */
        area = (B.getRe() - A.getRe()) * (C.getIm() - B.getIm());
    }

    /** toString() */
    public String toString() {
        String str = "";
        str += "D: " + D.toString() + "   ";
        str += "C: " + C.toString() + "\n";
        str += "A: " + A.toString() + "   ";
        str += "B: " + B.toString() + "\n";
        str += "area: " + area;
        return str;
    }

    /**
     * checkInside.
     *
     * * If the phase big -> small (ex. 1.9PI -> 0.1 PI, deltaPhi == -1.8PI) this
     * means that Re+ axis was crossed POSITIVELY, hence add 2PI to deltaPhi (-1.8PI
     * +2PI = 0.2PI, correct phase change).
     *
     * If the phase small -> big (ex. 0.1PI -> 1.9 PI, deltaPhi == 1.8PI) this means
     * that Re+ axis was crossed NEGATIVELY, hence substract 2PI from deltaPhi
     * (1.8PI - 2PI = -0.2PI, correct phase change)
     *
     * @return Bool: winding number close or greater than 1
     */
    protected Boolean checkInside(String f_z) {

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
                 * If phsase change is close to 2PI assume that positive x axis was crossed
                 * negtively and subtract 2PI from deltaPhi
                 */
                if (deltaPhi > 1.8 * Math.PI) {
                    deltaPhi -= 2 * Math.PI;
                }
                /**
                 * If phsase change is close to -2PI assume that positive x axis was crossed
                 * positively and add 2PI to deltaPhi
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
     * getChildren.
     *
     * Split the square into 4 equal children, enumerated starting bottom left going
     * clockwise
     *
     * @return chlidren the array with children
     */
    protected Solver[] getChildren() {
        Solver[] children = new Solver[4];
        children[0] = new Solver(A, AB_mid, MIDDLE, AD_mid, acc);
        children[1] = new Solver(AB_mid, B, BC_mid, MIDDLE, acc);
        children[2] = new Solver(MIDDLE, BC_mid, C, CD_mid, acc);
        children[3] = new Solver(AD_mid, MIDDLE, CD_mid, D, acc);
        return children;
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
    protected void solveInside(String f_z, ArrayList<Complex> solutions) {
        if (this.checkInside(f_z)) {
            if (this.area <= Math.pow(10, -2 * (this.acc.ordinal() + 3))) {
                solutions.add(this.MIDDLE);
            } else {
                Solver[] children = this.getChildren();
                for (Solver child : children) {
                    child.solveInside(f_z, solutions);
                }
            }
        }
    }

    /**
     * solve.
     *
     * Static method returning simplified arraylist of solutions to f_z inside s,
     * sorted in ascending order according to method described in
     * Complex.compareTo() function.
     *
     * TODO: create a version with only f_z as input and an "auto sized" solver.
     *
     * @param s   the solver to be used
     * @param f_z the function to solve
     *
     * @return solutions the list of solutions
     */
    public static ArrayList<Complex> solve(Solver s, String f_z) {

        /**
         * Get solutions inside s using recursive solveInside
         */
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        s.solveInside(f_z, solutions);

        /**
         * Round all soultions according to acc, delete duplicates and sort list
         */
        double rd;
        if (s.acc == Accuracy.LOW) {
            rd = 1000d;
        } else if (s.acc == Accuracy.MED) {
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

        /**
         * Remove duplicates
         */
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

        /**
         * Sort
         */
        Collections.sort(solutions);

        return solutions;
    }
}

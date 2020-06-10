package algorithm.solver;

import java.util.ArrayList;
import java.util.Collections;

import algorithm.parser.exception.CalculatorException;
import algorithm.parser.function.Complex;
import algorithm.parser.main.Parser;
import algorithm.parser.util.Variable;

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
 * In order to utilize this class use the static solve() function. All other
 * methods, including contructor, are private.
 *
 * @Author Piotr Machura (unless specified otherwise)
 */
public class Solver {

    /** Automatic range founder parameters */
    private static final int AUTO_RANGE_START = 5;
    private static final int AUTO_RANGE_INCREMENT = 5;
    private static final int AUTO_RANGE_MAX = 100;
    private static final int AUTO_RANGE_FAILED = 0;

    /** Algorithm adjustments */
    private static final double MAX_LEGAL_DELTAPHI = 1.8 * Math.PI;
    private static final double STEPS_PER_SIDELENGTH = 200;
    private static final double MIN_LEGAL_WINDING_NUMBER = 0.95 * 2 * Math.PI;
    private static final double MAX_LEGAL_ABS_OF_ROOT = 1;

    /** Output formatting adjustments */
    private static final double ROUNDER_LOW = 1000d;
    private static final double ROUNDER_MED = 10000d;
    private static final double ROUNDER_HIGH = 100000d;

    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    final SolverAccuracy accuracyLevel;
    final double MIN_LEGAL_AREA;

    /**
     * solve.
     *
     * Main method of the static Solver class. Finds roots of the complex function
     * f_z with max Re = range and max Im = range (in a rectangel of sidelength
     * 2*range).
     *
     * @param range         half of the sidelength of rectangle to look in.
     * @param f_z           the function to solve.
     * @param accuracyLevel the desired accuracyLevel.
     *
     * @return the list of solutions found inside range orted in ascending order
     *         according to method described in Complex.compareTo() function.
     */
    public static ArrayList<Complex> solve(final int range, final String f_z, final SolverAccuracy accuracyLevel) {
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        if (range == AUTO_RANGE_FAILED) {
            return solutions;
        } else {
            /**
             * To avoid roots and poles canceling each other: divide starting rectangle into
             * 64 tiny squares first and solve in each of them.
             */
            Solver[] children1 = new Solver(range, accuracyLevel).getChildren();
            for (Solver child1 : children1) {
                Solver[] children2 = child1.getChildren();
                for (Solver child2 : children2) {
                    Solver[] children3 = child2.getChildren();
                    for (Solver child3 : children3) {
                        child3.solveInside(f_z, solutions);
                    }
                }
            }

            /** Set up an approprieate rounder */
            double rd = ROUNDER_MED;
            if (accuracyLevel == SolverAccuracy.LOW) {
                rd = ROUNDER_LOW;
            } else if (accuracyLevel == SolverAccuracy.HIGH) {
                rd = ROUNDER_HIGH;
            }
            /** Round decimals according to accuracyLevel */
            for (int i = 0; i < solutions.size(); i++) {
                double reI = (double) Math.round(solutions.get(i).getRe() * rd) / rd;
                double imI = (double) Math.round(solutions.get(i).getIm() * rd) / rd;
                solutions.set(i, new Complex(reI, imI));
            }

            /** Remove duplicates */
            for (int i = 0; i < solutions.size() - 1; i++) {
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
            Collections.sort(solutions);
            return solutions;
        }

    }

    /**
     * solve (automatic verison).
     *
     * Starts with a range of AUTO_RANGE_START and adds AUTO_RANGE_INCREMENT until a
     * root is found. If a root is found invokes solve() with current range.
     * Terminates when AUTO_RANGE_MAX is reached and no roots were found.
     *
     * @param f_z           the function to solve
     * @param accuracyLevel the desired accuracyLevel
     *
     * @return solutions the formatted and sorted ArrayList of solutions
     */

    public static ArrayList<Complex> solve(final String f_z, final SolverAccuracy accuracyLevel) {
        int range = AUTO_RANGE_START;
        while (true) {
            Boolean isCurrentRangeValid = new Solver(range, accuracyLevel).checkWindingNumber(f_z);
            if (isCurrentRangeValid) {
                break;
            } else if (!isCurrentRangeValid) {
                if (range > AUTO_RANGE_MAX) {
                    range = AUTO_RANGE_FAILED;
                    break;
                } else {
                    range += AUTO_RANGE_INCREMENT;
                }
            }
        }
        return Solver.solve(range, f_z, accuracyLevel);
    }

    /**
     * Square constructor.
     *
     * Constructs a square using points A, B, C, D as shown in Solver.java header.
     *
     * @param a             the bottom-left point A
     * @param b             the bottom-right point B
     * @param c             the top-right point C
     * @param d             the top-left point D
     * @param accuracyLevel the desired accuracy level
     */
    private Solver(Complex a, Complex b, Complex c, Complex d, final SolverAccuracy accuracyLevel) {

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

        /** Calculate the minimum area allowed for this rectangle */
        MIN_LEGAL_AREA = Math.pow(10, -2 * (this.accuracyLevel.ordinal() + 3));
    }

    /**
     * Square constructor.
     *
     * Constructs a square with sidelength 2*range centered at the origin.
     *
     * @param range         half of the rectangle's sidelenght.
     * @param accuracyLevel the desired accuracy level.
     */
    private Solver(int range, final SolverAccuracy accuracyLevel) {
        A = new Complex(-range, -range);
        B = new Complex(range, -range);
        C = new Complex(range, range);
        D = new Complex(-range, range);
        this.accuracyLevel = accuracyLevel;

        /** Calculate mid-points based on given points */
        AB_mid = new Complex((B.getRe() + A.getRe()) / 2, A.getIm());
        BC_mid = new Complex(B.getRe(), (C.getIm() + B.getIm()) / 2);
        CD_mid = new Complex((C.getRe() + D.getRe()) / 2, C.getIm());
        AD_mid = new Complex(D.getRe(), (D.getIm() + A.getIm()) / 2);
        MIDDLE = new Complex((BC_mid.getRe() + AD_mid.getRe()) / 2, (CD_mid.getIm() + AB_mid.getIm()) / 2);

        /** Calculate area of the square */
        area = (B.getRe() - A.getRe()) * (C.getIm() - B.getIm());
        /** Calculate the minimum area allowed for this rectangle */
        MIN_LEGAL_AREA = Math.pow(10, -2 * (this.accuracyLevel.ordinal() + 3));
    }

    /**
     * deltaPhi.
     *
     * Calculates corrected phase change between two points.
     *
     * @param prevPhi the phase prior to step
     * @param nextPhi the phase after a step
     *
     * @return the phase change adjusted with MAX_LEGAL_DELTAPHI in mind
     */
    private double deltaPhi(double prevPhi, double nextPhi) {
        /** Calculate phase change */
        double deltaPhi = nextPhi - prevPhi;
        /**
         * If the phase went from small -> big (ex. 0.1PI -> 1.9 PI, deltaPhi == 1.8PI)
         * this means that Re+ axis was crossed NEGATIVELY, hence substract 2PI from
         * deltaPhi (1.8PI - 2PI = -0.2PI, correct phase change)
         */
        if (deltaPhi > MAX_LEGAL_DELTAPHI) {
            deltaPhi -= 2 * Math.PI;
        }
        /**
         * If the phase went from big -> small (ex. 1.9PI -> 0.1 PI, deltaPhi == -1.8PI)
         * this means that Re+ axis was crossed POSITIVELY, hence add 2PI to deltaPhi
         * (-1.8PI +2PI = 0.2PI, correct phase change).
         */
        else if (deltaPhi < -MAX_LEGAL_DELTAPHI) {
            deltaPhi += 2 * Math.PI;
        }
        return deltaPhi;
    }

    /**
     * checkWindingNumber.
     *
     * Check winding number in relation to function f_z.
     *
     * @return Bool: winding number close or greater than 1
     *
     * @Author Piotr Machura, Kacper Ledwosiński
     */
    private Boolean checkWindingNumber(final String f_z) {

        /** Step of "integration" - STEPS_PER_SIDELENGTH steps per side length */
        final double step = Math.sqrt(this.area) / STEPS_PER_SIDELENGTH;
        double windingNumber = 0;

        /** Starting point: A */
        double x = A.getRe();
        double y = A.getIm();
        /* Phase prior to step (to be remembered) */
        double prevPhi = 0;
        try {
            prevPhi = Complex.phase(Parser.eval(f_z, new Variable("z", new Complex(x, y))).getComplexValue());
        } catch (CalculatorException e) {
            prevPhi = 0;
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
                windingNumber += deltaPhi(prevPhi, nextPhi);
                prevPhi = nextPhi;
            } catch (Exception e) {
                /**
                 * This means a zero was encountered and phase cannot be calculated. If moving a
                 * step forward won't go outisde boundary then move, if it will go outisde
                 * boundary (which means you are at a corner) then proceed to checking next
                 * side.
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
                windingNumber += deltaPhi(prevPhi, nextPhi);
                prevPhi = nextPhi;
            } catch (Exception e) {
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
                windingNumber += deltaPhi(prevPhi, nextPhi);
                prevPhi = nextPhi;
            } catch (Exception e) {
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
                windingNumber += deltaPhi(prevPhi, nextPhi);
                prevPhi = nextPhi;
            } catch (Exception e) {
                if (y - step > A.getIm()) {
                    y -= step;
                } else {
                    break;
                }
            }
        }
        return Math.abs(windingNumber) > MIN_LEGAL_WINDING_NUMBER;
    }

    /**
     * getChildren.
     *
     * @return array of 4 equal children enumerated starting bottom left clockwise
     *
     * @Author Kacper Ledwosiński
     */
    private Solver[] getChildren() {
        Solver[] children = new Solver[4];
        children[0] = new Solver(A, AB_mid, MIDDLE, AD_mid, accuracyLevel);
        children[1] = new Solver(AB_mid, B, BC_mid, MIDDLE, accuracyLevel);
        children[2] = new Solver(MIDDLE, BC_mid, C, CD_mid, accuracyLevel);
        children[3] = new Solver(AD_mid, MIDDLE, CD_mid, D, accuracyLevel);
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
     *
     * @Author Piotr Machura, Kacper Ledwosiński
     */
    private void solveInside(final String f_z, ArrayList<Complex> solutions) {
        if (this.checkWindingNumber(f_z)) {
            if (this.area <= this.MIN_LEGAL_AREA) {
                /**
                 * Add MIDDLE to solutions if Abs(f_z(MIDDLE)) is small. This needs to be
                 * checked because this algorithm will detect both roots AND poles of a
                 * function.
                 */
                try {
                    double absOfRoot = Complex.abs(Parser.eval(f_z, new Variable("z", this.MIDDLE)).getComplexValue());
                    if (absOfRoot < MAX_LEGAL_ABS_OF_ROOT) {
                        solutions.add(this.MIDDLE);
                    }
                } catch (CalculatorException e) {
                    /**
                     * This means it's probably a pole too - do not add it and ignore.
                     */
                }
            } else {
                Solver[] children = this.getChildren();
                for (Solver child : children) {
                    child.solveInside(f_z, solutions);
                }
            }
        }
    }
}

package algorithm.solver;

/**
 * The class SolverDefaults.
 *
 * Contains default values used to calibrate the Solver class. They can be
 * ovverriden inside the Solver class using the readConfig function.
 *
 * @Author Piotr Machura
 */
public class SolverDefaults {
    /** Automatic range founder parameters */
    public static final int AUTO_RANGE_START = 5;
    public static final int AUTO_RANGE_INCREMENT = 5;
    public static final int AUTO_RANGE_MAX = 100;
    static final int AUTO_RANGE_FAILED = 0;

    /** Algorithm adjustments */
    public static final double MAX_LEGAL_DELTAPHI_RATIO = 1.8;
    public static final int STEPS_PER_SIDELENGTH = 200;
    public static final double MIN_LEGAL_WINDING_NUMBER_RATIO = 0.95;
    public static final double MAX_LEGAL_ABS_OF_ROOT = 1;

    /** Output formatting adjustments */
    static final double ROUNDER_LOW = 1000d;
    static final double ROUNDER_MED = 10000d;
    static final double ROUNDER_HIGH = 100000d;

    /** */
}
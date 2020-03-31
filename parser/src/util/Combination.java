/**
 * This is a library forked from https://github.com/sbesada/java.math.expression.parser
 * and slightly adapted to suit our needs. There is an appropriate comment  with "!"
 * wherever things were changed.
 */
package parser.src.util;

import parser.src.exception.CalculatorException;

/**
 * The Class Combination.
 */
public class Combination {

    /**
     * calc.
     *
     * @param m the m
     * @param n the n
     * @return the double
     * @throws CalculatorException the calculator exception
     */
    public static double calc(final int m, final int n) throws CalculatorException {
        if (n < 0) {
            throw new CalculatorException("n cannot be <0");
        }

        double result = 0.0;
        if (m == 0) {
            result = 0.0;
        } else {
            result = (double) Factorial.cal(m, false)
                    / (double) (Factorial.cal(m - n, false) * Factorial.cal(n, false));
        }

        return result;

    }

}

/**
 * This is a library forked from https://github.com/sbesada/java.math.expression.parser
 * and slightly adapted to suit our needs. There is an appropriate comment  with "!"
 * wherever things were changed.
 */
package parser.util;

import parser.exception.CalculatorException;

/**
 * The Class Factorial.
 */
public class Factorial {

    /**
     * cal.
     *
     * @param m         the m
     * @param valueZero the value zero
     * @return the int
     * @throws CalculatorException the calculator exception
     */
    public static int cal(final int m, final boolean valueZero) throws CalculatorException {
        if (m < 0) {
            throw new CalculatorException("the number must be greater than 0");
        }

        int result = 1;
        if (m == 0) {
            if (valueZero) {
                result = 0;
            } else {
                result = 1;
            }
        } else {
            for (int i = m; i > 0; i--) {
                result *= i;
            }
        }
        return result;
    }

}

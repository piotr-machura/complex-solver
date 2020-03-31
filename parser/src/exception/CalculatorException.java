/**
 * This is a library forked from https://github.com/sbesada/java.math.expression.parser
 * and slightly adapted to suit our needs. There is an appropriate comment  with "!"
 * wherever things were changed.
 */
package parser.src.exception;

/**
 * The Class CalculatorException.
 */
public class CalculatorException extends Exception {

    private static final long serialVersionUID = 6235428117353457356L;

    /**
     * CalculatorException.
     */
    public CalculatorException() {
        super();
    }

    /**
     * CalculatorException.
     *
     * @param message the message
     */
    public CalculatorException(final String message) {
        super(message);
    }
}

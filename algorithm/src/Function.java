package algorithm.src;

import java.util.ArrayList;

public class Function {

    ArrayList<Complex> solutions = new ArrayList<Complex>();
    String beforeParsing;

    public Complex solveFor(Complex z) {
        /*
         * Requires implementation of parsing function from string. So far it's just
         * f(z) = z for ease of math.
         */
        // TODO: Parsing implementation
        return z;
    }

    public void addSolution(Complex z) {
        solutions.add(z);
    }

    public String stringSolutions() {
        String out = "solutions: \n";
        for (Complex s : solutions) {
            out += s.toString() + "\n";
        }
        return out;
    }

}
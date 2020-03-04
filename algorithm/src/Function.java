package algorithm.src;

import java.util.ArrayList;

public class Function {

    ArrayList<Complex> solutions = new ArrayList<Complex>();

    public Complex solveFor(Complex z) {
        z = z.times(z).minus(new Complex(1, 0));
        z = new Complex(z.re(), z.im());
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
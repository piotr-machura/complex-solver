package algorithm.src;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        Complex A = new Complex(-2, -2);
        Complex B = new Complex(2, -2);
        Complex C = new Complex(2, 2);
        Complex D = new Complex(-2, 2);

        Rectangle r1 = new Rectangle(A, B, C, D);

        Function f = new Function();
        r1.solveInside(f);
        
        System.out.println(f.stringSolutions());

    }
}
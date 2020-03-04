package algorithm;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Point A = new Point(-20, -20);
        Point B = new Point(20, -20);
        Point C = new Point(20, 20);
        Point D = new Point(-20, 20);
        Rectangle r1 = new Rectangle(A, B, C, D);

        System.out.print(r1.toString() + "\n\n");
        Rectangle[] split = r1.splitThis();
        for (Rectangle rectangle : split) {
            System.out.println(rectangle.toString() + "\n\n");
        }
        Function f = new Function();
        r1.solveInside(f);
        System.out.println(f.stringSolutions());

    }
}
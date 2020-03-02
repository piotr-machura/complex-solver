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
        System.out.print(r1.toString());
    }
}
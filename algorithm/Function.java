package algorithm;

/**
 * Function, so far just z^2
 */
public class Function {
    public Function() {
    }

    public Point solveFor(Point p) {
        return new Point(p.X * p.X, p.Y * p.Y);
    }

    public Point solveFor(double x, double y) {
        return new Point(x * x, y * y);
    }

}
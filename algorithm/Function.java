package algorithm;

import java.util.ArrayList;

/**
 * Function, so far just (x, y) -> (x^2, y^2)
 */
// TODO: Implement function setting
public class Function {
    ArrayList<Point> solutions;

    public Function() {
        solutions = new ArrayList<Point>();
    }

    public Point solveFor(Point p) {
        return new Point(p.X * p.X, p.Y * p.Y);
    }

    public Point solveFor(double x, double y) {
        return new Point(x * x, y * y);
    }

    public void addSolution(Point p) {
        solutions.add(p);
    }

}
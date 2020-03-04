package algorithm.src;

import java.util.ArrayList;

/**
 * Function, so far just (x, y) -> (x, y)
 */
// TODO: Implement function setting
public class Function {
    ArrayList<Point> solutions;

    public Function() {
        solutions = new ArrayList<Point>();
    }

    public Point solveFor(Point p) {
        return new Point(p.X, p.Y);
    }

    public Point solveFor(double x, double y) {
        return new Point(x, y);
    }

    public void addSolution(Point p) {
        solutions.add(p);
    }

    public String stringSolutions() {
        String out = "";
        for (Point point : solutions) {
            out += point.toString();
        }
        return out;
    }

}
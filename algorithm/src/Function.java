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
        return new Point(p.X+1, p.Y+1);
    }

    public Point solveFor(double x, double y) {
        return new Point(x+1, y+1);
    }

    public void addSolution(Point p) {
        solutions.add(p);
    }

    public String stringSolutions() {
        String out = "test: ";
        for (Point point : solutions) {
            out += point.toString();
        }
        return out;
    }

}
package algorithm.src;

import java.lang.Math;

/**
 * Point
 */
public class Point {

    double X, Y, PHI, R;

    public Point(double x, double y) {
        X = x;
        Y = y;
        PHI = Math.atan2(Y, X);
        R = Math.sqrt(X * X + Y * Y);
    }

    public String toString() {
        return "(" + X + "," + Y + ")";
    }

}
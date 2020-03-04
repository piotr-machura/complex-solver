// Rectangle

// D--CD_mid--C
// |          |
//AD_mid     BC_mid
// |          |
// A--AB_mid--B

public class Rectangle {
    Point A, B, C, D;
    Point AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;

    public Rectangle(Point a, Point b, Point c, Point d) {
        A = a;
        B = b;
        C = c;
        D = d;
        //TODO: tick d determined by size of rect
        AB_mid = new Point(B.X - A.X, A.Y);
        BC_mid = new Point(B.X, C.Y - B.Y);
        CD_mid = new Point(D.X - C.X, C.Y);
        AD_mid = new Point(D.X, D.Y - A.Y);
        MIDDLE = new Point(BC_mid.X - AD_mid.X, CD_mid.Y - AB_mid.Y);
        area = (B.X - A.X) * (C.Y - B.Y);
    }

    public String toString() {
        String rectString = "";
        rectString += "C: " + C.toString() + " ";
        rectString += "D: " + D.toString() + "\n";
        rectString += "A: " + A.toString() + " ";
        rectString += "B: " + B.toString();
        return rectString;
    }

    public boolean checkInside(Function f) {
        boolean hasZeroes = false;
        //TODO: change to windingNumber
        double cNumber = 0;
        // d - step of "integration"
        //TODO: d depends on rect size
        double d = 0.001;
        // Starting point: A
        double x = A.X;
        double y = A.Y;
        Point p_cur = f.solveFor(x, y);
        // Path A -> B
        while (x < B.X) {
            x += d;
            Point p_next = f.solveFor(x, y);
            //TODO: change kolejność
            cNumber += p_cur.PHI - p_next.PHI;
            p_cur = p_next;
        }
        // Path B -> C
        while (y < C.Y) {
            y += d;
            Point p_next = f.solveFor(x, y);
            cNumber += p_cur.PHI - p_next.PHI;
        }
        // Path C -> D
        while (x > D.X) {
            x -= d;
            Point p_next = f.solveFor(x, y);
            cNumber += p_cur.PHI - p_next.PHI;
        }
        // Path D -> A
        while (y > A.Y) {
            y -= d;
            Point p_next = f.solveFor(x, y);
            cNumber += p_cur.PHI - p_next.PHI;
        }
        if (cNumber < 0.001) {
            hasZeroes = true;
        }
        return hasZeroes;
    }

    // ArrayList lets dynamicly add objects to it (solutions)
    public void solveInside(Function f) {
        if (checkInside(f)) {
            if (area <= 0.001) {
                f.addSolution(this.MIDDLE);
            } else {
                // TODO Póki co to dzieli tylko "pionowo", a powinno na cztery
                Rectangle split1 = new Rectangle(A, AB_mid, CD_mid, D);
                Rectangle split2 = new Rectangle(AB_mid, B, C, CD_mid);
                Rectangle split3 = new Rectangle()
                split1.solveInside(f);
                split2.solveInside(f);
            }
        }
    }

}

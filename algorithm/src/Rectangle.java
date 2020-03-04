package algorithm.src;

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
        // TODO: tick d determined by size of rect
        AB_mid = new Point((B.X + A.X) / 2, A.Y);
        BC_mid = new Point(B.X, (C.Y + B.Y) / 2);
        CD_mid = new Point((C.X + D.X) / 2, C.Y);
        AD_mid = new Point(D.X, (D.Y + A.Y) / 2);
        MIDDLE = new Point((BC_mid.X + AD_mid.X) / 2, (CD_mid.Y + AB_mid.Y) / 2);
        area = (B.X - A.X) * (C.Y - B.Y);
    }

    public String toString() {
        String rectString = "";
        rectString += "D: " + D.toString() + "   ";
        rectString += "C: " + C.toString() + "\n";
        rectString += "A: " + A.toString() + "   ";
        rectString += "B: " + B.toString();
        return rectString;
    }

    public boolean checkInside(Function f) {
        boolean hasZeroes = true;

        // d - step of "integration"
        // TODO: d depends on rect size
        double d = 0.001;
        
        // Starting point: A
        double x = A.X;
        double y = A.Y;
        
        double windingNumber = 0;

        while(x<B.X) {
            windingNumber += f.solveFor(x, y).PHI;
            x+=d;
        }

        while(y<C.Y) {
            windingNumber += f.solveFor(x,y).PHI;
            y+=d;
        }

        while(x>D.X) {
            windingNumber += f.solveFor(x,y).PHI;
            x-=d;
        }

        while(y>A.Y) {
            windingNumber += f.solveFor(x,y).PHI;
            y-=d;
        }

        System.out.println("Winding number: " + windingNumber + " \n");
        if (windingNumber < 0.001) {
            hasZeroes = false;
        }
        
        return hasZeroes;
    }

    Rectangle[] splitThis() {
        Rectangle[] split = new Rectangle[4];
        split[0] = new Rectangle(A, AB_mid, MIDDLE, AD_mid);
        split[1] = new Rectangle(AB_mid, B, BC_mid, MIDDLE);
        split[2] = new Rectangle(MIDDLE, BC_mid, C, CD_mid);
        split[3] = new Rectangle(AD_mid, MIDDLE, CD_mid, D);
        return split;
    }

    public void solveInside(Function f) {
        System.out.println("Checking rectangle: \n" + toString() + "\n");
        if (checkInside(f)) {
            if (area <= 0.1) {
                f.addSolution(this.MIDDLE);
            } else {
                Rectangle[] split = splitThis();
                for (Rectangle rectangle : split) {
                    rectangle.solveInside(f);
                }
            }
        }
    }

}

package algorithm.src;

/*
        Rectangle
    D-----CD_mid-----C
    |                |
  AD_mid   MIDDLE   BC_mid
    |                |
    A-----AB_mid-----B
*/

public class Rectangle {
    Complex A, B, C, D;
    Complex AB_mid, BC_mid, CD_mid, AD_mid, MIDDLE;
    double area;
    InputSpace space;
    OutputSpace output;

    public Rectangle(Complex a, Complex b, Complex c, Complex d, InputSpace space, OutputSpace output) {
        /*
         * Constructs a rectangle using points A, B, C, D bound to provided input and
         * output spaces.
         */
        A = a;
        B = b;
        C = c;
        D = d;
        this.space = space;
        this.output = output;

        // Calculating mid-points based on given points
        AB_mid = new Complex((B.re + A.re) / 2, A.im);
        BC_mid = new Complex(B.re, (C.im + B.im) / 2);
        CD_mid = new Complex((C.re + D.re) / 2, C.im);
        AD_mid = new Complex(D.re, (D.im + A.im) / 2);
        MIDDLE = new Complex((BC_mid.re + AD_mid.re) / 2, (CD_mid.im + AB_mid.im) / 2);

        // Calculating area of rectangle
        area = (B.re - A.re) * (C.im - B.im);
    }

    public String toString() {
        String rectString = "";
        rectString += "D: " + D.toString() + "   ";
        rectString += "C: " + C.toString() + "\n";
        rectString += "A: " + A.toString() + "   ";
        rectString += "B: " + B.toString() + "\n";
        rectString += "area: " + area;
        return rectString;
    }

    // ! Btw to sie zesrało, nie wiem dlaczego ale wiem w którym momencie
    public Boolean checkInside(Function f) {
        /*
         * Checks if winding number of a given rectangle is not close to zero.
         */

        // Tick of "integration" - 1/10th of side length
        double d = Math.sqrt(this.area) / 10;
        double windingNumber = 0;
        System.out.println("Tick: " + d + "\n");

        // Starting number: A
        double x = A.re;
        double y = A.im;

        // Path A->B (going right)
        System.out.println("\nA -> B");
        while (x < B.re) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            x += d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println("zmiana o" + (now - prev));
        }

        // Path B->C (going up)
        System.out.println("B -> C");
        while (y < C.im) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            y += d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println("zmiana o" + (now - prev));
        }

        // Path C->D (going left)
        System.out.println("C -> D");
        while (x > D.re) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            x -= d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            System.out.println("zmiana o" + (now - prev));
        }

        // Path D->A (going down)
        System.out.println("D -> A");
        while (y > A.im) {
            space.addPoint(new Complex(x, y));
            double prev = f.solveFor(new Complex(x, y)).phase();
            y -= d;
            double now = f.solveFor(new Complex(x, y)).phase();
            windingNumber += now - prev;
            // !!! Ten komentarz jest aktualny dla funkcji f(z)=z
            /*
             * Tutaj jest problem: (-2.0 - 0.8i) -> (-2.0 - 0.4i) faza zmienia sie o -2pi i
             * zeruje winding number. Nie mam kurde pojęcia dlaczego, bo:
             * arctan(0.4/2)-arctan(0.8/2) = -0.183 a nie kurde -2pi, a ten wynik w miare
             * pasuje do reszty (zmiany są po ok. 0.2 na tick)
             */
            System.out.println(new Complex(x, y - d) + " -> " + new Complex(x, y) + " zmiana o: " + (now - prev));
        }

        // Total number of revolutions = (total phase change) / 2 PI
        windingNumber = windingNumber / (2 * Math.PI);
        System.out.println("Winding number: " + windingNumber + "\n\n");

        // Checks if winding number sufficiently bigger than zero
        final double epsilon = 0.01;
        return Math.abs(windingNumber) > epsilon;
    }

    Rectangle[] getChildren() {
        /*
         * Splits the rectangle into 4 children, enumerated starting bottom left
         * clockwise
         */
        Rectangle[] children = new Rectangle[4];
        children[0] = new Rectangle(A, AB_mid, MIDDLE, AD_mid, space, output);
        children[1] = new Rectangle(AB_mid, B, BC_mid, MIDDLE, space, output);
        children[2] = new Rectangle(MIDDLE, BC_mid, C, CD_mid, space, output);
        children[3] = new Rectangle(AD_mid, MIDDLE, CD_mid, D, space, output);
        return children;
    }

    public void solveInside(Function f) {
        /*
         * Recursively checks rectangle's winding number, splitting it into 4 children
         * if it's big and viable and discarding it if it's not viable. If it's small
         * and viable, adds it's middle to f's solution list
         */
        System.out.println("Checking rectangle: \n" + toString() + "\n");
        if (this.checkInside(f)) {
            if (this.area <= 0.001) {
                f.addSolution(this.MIDDLE);
            } else {
                Rectangle[] children = this.getChildren();
                for (Rectangle child : children) {
                    child.solveInside(f);
                }
            }
        }
    }

}

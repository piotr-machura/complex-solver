/**
 * This is a library forked from https://github.com/sbesada/java.math.expression.parser
 * and slightly adapted to suit our needs. There is an appropriate comment  with "!"
 * wherever things were changed.
 */
package parser.function;

import parser.exception.CalculatorException;

/**
 * Complex.
 */
public class Complex implements Comparable<Complex> {

    /** real. */
    private double re;

    /** imaginary. */
    private double im;

    /**
     * Complex.
     *
     * @param re the re
     *
     * @param im the im
     */
    public Complex(final double re, final double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Complex.
     */
    public Complex() {
        re = 0.0;
        im = 0.0;
    }

    /**
     * Gets the r.
     *
     * @return the r
     */
    public double getRe() {
        return re;
    }

    /**
     * Sets the re.
     *
     * @param re the new re
     */
    public void setRe(final double re) {
        this.re = re;
    }

    /**
     * Gets the im.
     *
     * @return the im
     */
    public double getIm() {

        return im;
    }

    /**
     * Sets the im.
     *
     * @param im the new im
     */
    public void setIm(final double im) {
        this.im = im;
    }

    /**
     * toString.
     *
     * ! Made by: Piotr Machura for the purpouses of winding number algorithm
     */
    public String toString() {
        if (im == 0)
            return re + "";
        if (re == 0)
            return im + "i";
        if (im < 0)
            return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /**
     * phase.
     *
     * ! Made by: Piotr Machura for the purpouses of winding number algorithm
     *
     * Returns phase as phi +2kPI, where phi = [0, 2pi], throws CalculatorException
     * for point 0+0i
     *
     * @ param k the k-th phase
     *
     * @ return the phase
     *
     * @ throws CalculatorException the calculator exception
     */
    public static double phase(Complex z) throws CalculatorException {
        double y = Math.abs(z.im);
        double x = Math.abs(z.re);
        if (z.re > 0 && z.im == 0) {
            return 0;
        } else if (z.re > 0 && z.im == 0) {
            return 0;
        } else if (z.re > 0 && z.im > 0) {
            return Math.atan(y / x);
        } else if (z.re == 0 && z.im > 0) {
            return 0.5 * Math.PI;
        } else if (z.re < 0 && z.im > 0) {
            return Math.atan(x / y) + Math.PI / 2;
        } else if (z.re < 0 && z.im == 0) {
            return Math.PI;
        } else if (z.re < 0 && z.im < 0) {
            return Math.atan(y / x) + Math.PI;
        } else if (z.re == 0 && z.im < 0) {
            return 1.5 * Math.PI;
        } else if (z.re > 0 && z.im < 0) {
            return Math.atan(x / y) + 1.5 * Math.PI;
        } else {
            throw new CalculatorException("Phase undefined for point " + z);
        }
    }

    /**
     * add.
     *
     * @param a the a
     *
     * @param b the b
     *
     * @return the complex
     */
    public static Complex add(final Complex a, final Complex b) {
        final double real = a.re + b.re;
        final double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     * add.
     *
     * @param real the real
     *
     * @param c    the c
     *
     * @return the complex
     */
    public static Complex add(final double real, final Complex c) {
        return new Complex(c.re + real, c.im);
    }

    /**
     * sub.
     *
     * @param a the a
     *
     * @param b the b
     *
     * @return the complex
     */
    public static Complex sub(final Complex a, final Complex b) {
        final double real = a.re - b.re;
        final double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * Sub.
     *
     * @param real the real
     *
     * @param c    the c
     *
     * @return the complex
     */
    public static Complex sub(final double real, final Complex c) {
        return new Complex(c.re - real, c.im);
    }

    /**
     * multiply.
     *
     * @param a the a
     *
     * @param b the b
     *
     * @return the complex
     */
    public static Complex mul(final Complex a, final Complex b) {
        final double real = (a.re * b.re) - (a.im * b.im);
        final double imag = (a.im * b.re) + (a.re * b.im);
        return new Complex(real, imag);
    }

    /**
     * conjugate.
     *
     * @param c the c
     *
     * @return the complex
     */
    public static Complex conjugate(final Complex c) {
        return new Complex(c.re, -c.im);
    }

    /**
     * div.
     *
     * @param a the a
     *
     * @param b the b
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public static Complex div(final Complex a, final Complex b) throws CalculatorException {

        if ((b.re == 0) && (b.im == 0)) {
            throw new CalculatorException("The complex number b is 0");
        }

        final double c = Math.pow(b.re, 2);
        final double d = Math.pow(b.im, 2);

        double real;
        double imag;
        real = (a.re * b.re) + (a.im * b.im);
        real /= (c + d);
        imag = (a.im * b.re) - (a.re * b.im);
        imag /= (c + d);

        return new Complex(real, imag);
    }

    /**
     * abs.
     *
     * @param z the z
     *
     * @return the double
     */
    public static double abs(final Complex z) {
        double x, y, ans, temp;
        x = Math.abs(z.re);
        y = Math.abs(z.im);
        if (x == 0.0) {
            ans = y;
        } else if (y == 0.0) {
            ans = x;
        } else if (x > y) {
            temp = y / x;
            ans = x * Math.sqrt(1.0 + (temp * temp));
        } else {
            temp = x / y;
            ans = y * Math.sqrt(1.0 + (temp * temp));
        }
        return ans;
    }

    /**
     * sqrt.
     *
     * @param c the c
     *
     * @return the complex
     */
    public static Complex sqrt(final Complex c) {
        double real, imag;
        double x, y, w, r;

        Complex result = null;

        if ((c.re == 0.0) && (c.im == 0.0)) {
            result = new Complex();
        } else {
            x = Math.abs(c.re);
            y = Math.abs(c.im);
            if (x >= y) {
                r = y / x;
                w = Math.sqrt(x) * Math.sqrt(0.5 * (1.0 + Math.sqrt(1.0 + (r * r))));
            } else {
                r = x / y;
                w = Math.sqrt(y) * Math.sqrt(0.5 * (r + Math.sqrt(1.0 + (r * r))));
            }
            if (c.re >= 0.0) {
                real = w;
                imag = c.im / (2.0 * w);
            } else {
                imag = (c.im >= 0) ? w : -w;
                real = c.im / (2.0 * imag);
            }
            result = new Complex(real, imag);
        }

        return result;
    }

    /**
     * Complex.
     *
     * @param x the x
     *
     * @param c the c
     *
     * @return the complex
     */
    public static Complex mul(final double x, final Complex c) {
        final Complex result = new Complex();
        result.re = c.re * x;
        result.im = c.im * x;
        return result;
    }

    /**
     * div.
     *
     * @param x the x
     *
     * @param c the c
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public static Complex div(final double x, final Complex c) throws CalculatorException {
        if (x == 0) {
            throw new CalculatorException("scalar is 0");
        }
        final Complex result = new Complex();
        result.re = c.re / x;
        result.im = c.im / x;
        return result;
    }

    /**
     * inverse.
     *
     * @return the complex
     */
    public Complex inverse() {
        final Complex result = new Complex();
        final double a = re * re;
        final double b = im * im;
        if ((a == 0) && (b == 0)) {
            result.re = 0;
            result.im = 0;
        } else {
            result.re = re / (a + b);
            result.im = im / (a + b);
        }
        return result;

    }

    /**
     * pow.
     *
     * @param c   the c
     *
     * @param exp the exp
     *
     * @return the complex
     */
    /**
     * public static Complex pow(final Complex c, final int exp) throws
     * CalculatorException { double x = 0.0, y = 0.0; int sign; for (int i = 0; i <=
     * exp; i++) { sign = ((i % 2) == 0) ? 1 : -1; // real x +=
     * Combination.calc(exp, 2 * i) * Math.pow(c.r, exp - (2 * i)) * Math.pow(c.i, 2
     * * i) * sign; if (exp == (2 * i)) { break; } // imaginary y +=
     * Combination.calc(exp, (2 * i) + 1) * Math.pow(c.r, exp - ((2 * i) + 1)) *
     * Math.pow(c.i, (2 * i) + 1) sign; } return new Complex(x, y); }
     */
    /**
     *
     * pow
     *
     * @param c
     *
     * @param exp
     *
     * @return
     */
    public static Complex pow(final Complex c, final Double exp) {
        return c.pow(exp);
    }

    /**
     * power.
     *
     * @param c   the c
     *
     * @param exp the exp
     *
     * @return the complex
     */
    public static Complex pow(final Complex c, final Complex exp) {
        return c.pow(exp);
    }

    /**
     * module.
     *
     * @return the double
     */
    public double module() {
        return Math.sqrt((re * re) + (im * im));
    }

    /**
     * arg.
     *
     * @return the double
     */
    public double arg() {

        double angle = Math.atan2(im, re);
        if (angle < 0) {
            angle = (2 * Math.PI) + angle;
        }
        return (angle * 180) / Math.PI;

    }

    /**
     * negate.
     *
     * @return the complex
     */
    public Complex negate() {
        return new Complex(-re, -im);
    }

    /**
     * exp.
     *
     * @return the complex
     */
    // E^c
    public Complex exp() {
        final double exp_x = Math.exp(re);
        return new Complex(exp_x * Math.cos(im), exp_x * Math.sin(im));
    }

    /**
     * log10().
     *
     * @return the complex
     */
    public Complex log10() {

        final double rpart = Math.sqrt((re * re) + (im * im));
        double ipart = Math.atan2(im, re);
        if (ipart > Math.PI) {
            ipart = ipart - (2.0 * Math.PI);
        }
        return new Complex(Math.log10(rpart), (1 / Math.log(10)) * ipart);

    }

    /**
     * log natural log.
     *
     * @return the complex
     */
    public Complex log() {
        return new Complex(Math.log(abs(this)), Math.atan2(im, re));

    }

    /**
     * sqrt.
     *
     * @return the complex
     */
    public Complex sqrt() {
        final double r = Math.sqrt((this.re * this.re) + (im * im));
        final double rpart = Math.sqrt(0.5 * (r + this.re));
        double ipart = Math.sqrt(0.5 * (r - this.re));
        if (im < 0.0) {
            ipart = -ipart;
        }
        return new Complex(rpart, ipart);
    }

    /**
     * Cbrt.
     *
     * @param a the a
     *
     * @return the complex
     */
    public static Complex cbrt(final Complex a) {
        Complex z = new Complex();
        if (a.im != 0.0) {
            z.re = Math.cbrt(abs(a)) * Math.cos(a.arg() / 3.0);
            z.im = Math.cbrt(abs(a)) * Math.sin(a.arg() / 3.0);
        } else {
            z = new Complex(Math.cbrt(a.re), 0);
        }
        return z;
    }

    /**
     * pow.
     *
     * @param exp the exp
     *
     * @return the complex
     */
    public Complex pow(final Complex exp) {
        Complex a = this.log();
        a = mul(exp, a);
        return a.exp();
    }

    /**
     * pow.
     *
     * @param exp the exp
     *
     * @return the complex
     */
    public Complex pow(final double exp) {
        Complex a = this.log();
        a = mul(exp, a);
        return a.exp();
    }

    /**
     * sin.
     *
     * @return the complex
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    /**
     * cos.
     *
     * @return the complex
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -StrictMath.sin(re) * Math.sinh(im));
    }

    /**
     * tan.
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public Complex tan() throws CalculatorException {
        return div(this.sin(), this.cos());
    }

    /**
     * asin.
     *
     * @return the complex
     */
    public Complex asin() {
        final Complex IM = new Complex(0.0, -1.0);
        final Complex ZP = mul(this, IM);
        final Complex ZM = add((sub(new Complex(1.0, 0.0), mul(this, this))).sqrt(), ZP);
        return mul(ZM.log(), new Complex(0.0, 1.0));
    }

    /**
     * acos.
     *
     * @return the complex
     */
    public Complex acos() {
        final Complex IM = new Complex(0.0, -1.0);
        final Complex ZM = add(mul((sub(new Complex(1.0, 0.0), mul(this, this))).sqrt(), IM), this);
        return mul(ZM.log(), new Complex(0.0, 1.0));
    }

    /**
     * atan.
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public Complex atan() throws CalculatorException {
        final Complex IM = new Complex(0.0, -1.0);
        final Complex ZP = new Complex(re, im - 1.0);
        final Complex ZM = new Complex(-re, -im - 1.0);
        return div(2.0, mul(IM, (div(ZP, ZM).log())));
    }

    /**
     * sinh.
     *
     * @return the complex
     */
    public Complex sinh() {
        return new Complex(Math.sinh(re) * Math.cos(im), Math.cosh(re) * Math.sin(im));
    }

    /**
     * cosh.
     *
     * @return the complex
     */
    public Complex cosh() {
        return new Complex(Math.cosh(re) * Math.cos(im), Math.sinh(re) * Math.sin(im));
    }

    /**
     * tanh.
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public Complex tanh() throws CalculatorException {
        return div(this.sinh(), this.cosh());
    }

    /**
     * atanh.
     *
     * @return the complex
     *
     * @throws CalculatorException the calculator exception
     */
    public Complex atanh() throws CalculatorException {
        return sub((add(1.0, this)).log(), div(2.0, ((sub(1.0, this)).negate()).log()));
    }

    @Override
    public int compareTo(Complex comp) {
        if (this.re > comp.re) {
            return 1;
        } else if (this.re < comp.re) {
            return -1;
        } else if (this.re == comp.re && this.im < comp.im) {
            return -1;
        } else if (this.re == comp.re && this.im > comp.im) {
            return 1;
        } else {
            /** this.re == comp.re && this.im == comp.im */
            return 0;
        }
    }

}

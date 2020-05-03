package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import algorithm.Rectangle;
import algorithm.Rectangle.AcLevel;
import parser.function.Complex;

public class TestRectangleSimple {
    static Complex A = new Complex(-5, -5);
    static Complex B = new Complex(5, -5);
    static Complex C = new Complex(5, 5);
    static Complex D = new Complex(-5, 5);

    @Test
    public void testRectangleSimple_1() {
        String f_z = "z";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_2() {
        String f_z = "z";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_3() {
        String f_z = "z";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        System.out.println(solutions);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testRectangleSimple_4() {
        String f_z = "sin(z)";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(-Math.PI, 0), new Complex(0, 0), new Complex(Math.PI, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_5() {
        String f_z = "sin(z)";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = { new Complex(-Math.PI, 0), new Complex(0, 0), new Complex(Math.PI, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_6() {
        String f_z = "sin(z)";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = { new Complex(-Math.PI, 0), new Complex(0, 0), new Complex(Math.PI, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testRectangleSimple_7() {
        String f_z = "e^z";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = {};

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_8() {
        String f_z = "e^z";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = {};

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_9() {
        String f_z = "e^z";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = {};

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testRectangleSimple_10() {
        String f_z = "e^z-1";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_11() {
        String f_z = "e^z-1";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_12() {
        String f_z = "e^z-1";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testRectangleSimple_13() {
        String f_z = "z-i";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(0, 1) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_14() {
        String f_z = "z-i";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = { new Complex(0, 1) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_15() {
        String f_z = "z-i";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = { new Complex(0, 1) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testRectangleSimple_16() {
        String f_z = "z^2+z-i";
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(-1.30024, -0.624811), new Complex(0.300243, 0.624811) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testRectangleSimple_17() {
        String f_z = "z^2+z-i";
        AcLevel acc = AcLevel.MED;
        Complex[] expected = { new Complex(-1.30024, -0.624811), new Complex(0.300243, 0.624811) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testRectangleSimple_18() {
        String f_z = "z^2+z-i";
        AcLevel acc = AcLevel.HIGH;
        Complex[] expected = { new Complex(-1.30024, -0.624811), new Complex(0.300243, 0.624811) };

        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }
}
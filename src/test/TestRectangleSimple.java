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
        System.out.print("HI");
        AcLevel acc = AcLevel.LOW;
        Complex[] expected = { new Complex(0, 0) };
        ArrayList<Complex> solutions = new ArrayList<Complex>();
        new Rectangle(A, B, C, D, acc).solveInside(f_z, solutions);
        Rectangle.cleanUpSolutions(solutions, acc);
        assertEquals(expected.length, solutions.size());
        assertEquals(expected[0].getRe(), solutions.get(0).getRe(), 0.001);
        assertEquals(expected[0].getIm(), solutions.get(0).getIm(), 0.001);
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
        assertEquals(expected[0].getRe(), solutions.get(0).getRe(), 0.0001);
        assertEquals(expected[0].getIm(), solutions.get(0).getIm(), 0.0001);
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
        assertEquals(expected[0].getRe(), solutions.get(0).getRe(), 0.00001);
        assertEquals(expected[0].getIm(), solutions.get(0).getIm(), 0.00001);
    }
}
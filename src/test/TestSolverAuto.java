package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import algorithm.solver.Solver;
import algorithm.solver.SolverAccuracy;
import algorithm.parser.function.Complex;

/**
 * Test "automatic range" Solver.solve() variant. Note that "expected" arrays
 * are sorted in ascending order according to method described in
 * Complex.compareTo() function. Test for each function is conducted 3 times at
 * each accuracy level to ensure the right amount of significant digits.
 *
 * @Author Piotr Machura
 */
public class TestSolverAuto {

    @Test
    public void testSolverAuto_1() {
        String f_z = "z";
        SolverAccuracy acc = SolverAccuracy.LOW;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testSolverAuto_2() {
        String f_z = "z";
        SolverAccuracy acc = SolverAccuracy.MED;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testSolverAuto_3() {
        String f_z = "z";
        SolverAccuracy acc = SolverAccuracy.HIGH;
        Complex[] expected = { new Complex(0, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testSolverAuto_4() {
        String f_z = "z-10";
        SolverAccuracy acc = SolverAccuracy.LOW;
        Complex[] expected = { new Complex(10, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testSolverAuto_5() {
        String f_z = "z-10";
        SolverAccuracy acc = SolverAccuracy.MED;
        Complex[] expected = { new Complex(10, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testSolverAuto_6() {
        String f_z = "z-10";
        SolverAccuracy acc = SolverAccuracy.HIGH;
        Complex[] expected = { new Complex(10, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testSolverAuto_7() {
        String f_z = "z-99";
        SolverAccuracy acc = SolverAccuracy.LOW;
        Complex[] expected = { new Complex(99, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testSolverAuto_8() {
        String f_z = "z-99";
        SolverAccuracy acc = SolverAccuracy.MED;
        Complex[] expected = { new Complex(99, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testSolverAuto_9() {
        String f_z = "z-99";
        SolverAccuracy acc = SolverAccuracy.HIGH;
        Complex[] expected = { new Complex(99, 0) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }

    @Test
    public void testSolverAuto_10() {
        String f_z = "z-99*i";
        SolverAccuracy acc = SolverAccuracy.LOW;
        Complex[] expected = { new Complex(0, 99) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.001);
        }
    }

    @Test
    public void testSolverAuto_11() {
        String f_z = "z-99*i";
        SolverAccuracy acc = SolverAccuracy.MED;
        Complex[] expected = { new Complex(0, 99) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.0001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.0001);
        }
    }

    @Test
    public void testSolverAuto_12() {
        String f_z = "z-99*i";
        SolverAccuracy acc = SolverAccuracy.HIGH;
        Complex[] expected = { new Complex(0, 99) };

        ArrayList<Complex> solutions = Solver.solve(f_z, acc);
        assertEquals(expected.length, solutions.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getRe(), solutions.get(i).getRe(), 0.00001);
            assertEquals(expected[i].getIm(), solutions.get(i).getIm(), 0.00001);
        }
    }
}
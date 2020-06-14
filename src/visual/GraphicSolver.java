package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import algorithm.parser.exception.CalculatorException;
import algorithm.parser.function.Complex;
import algorithm.parser.main.Parser;
import algorithm.parser.util.Variable;
import algorithm.solver.Solver;
import algorithm.solver.SolverAccuracy;

/**
 * @Author Kacper Ledwosiński
 */
public class GraphicSolver extends JPanel
        implements MouseMotionListener, MouseListener, MouseWheelListener, ActionListener, Runnable {
    private static final long serialVersionUID = 1L;

    enum Direction {
        RIGHT, DOWN, LEFT, UP, STOP
    }

    int originX = -1, originY = -1;
    int offX, offY;
    int scrollSpeed = 2;
    int centerX = 10, centerY = 10;
    float zoomX = 200, zoomY = 200;
    float scaleX, scaleY;
    String f;
    ArrayList<Complex> sq_points = new ArrayList<Complex>();
    boolean panning = false;

    double tickX, tickY;

    /** Animation components */
    Direction dir = Direction.RIGHT; // 0-right, 1-down, 2-left, 3-up
    Complex A, B, C, D;
    ArrayList<Complex[]> childPosition = new ArrayList<Complex[]>();
    int range;
    double res; // rect resolution - difference between points
    int divideDeep = 6;
    int deepCounter = 0;
    Timer graphicTime;
    Boolean newSquare = true;

    int speed = 0; // ms timer delay

    ExecutorService graphicExec = Executors.newSingleThreadExecutor();

    public JFrame outFrame;
    OutputSpace outputSpace;

    public GraphicSolver(final String f, final int range) {
        this.f = f;
        this.range = range;
        scaleX = range;
        scaleY = range;

        tickX = scaleX / zoomX;
        tickY = scaleY / zoomY;

        res = tickX;

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);

        A = new Complex(-range, -range);
        B = new Complex(range, -range);
        C = new Complex(range, range);
        D = new Complex(-range, range);

        childPosition = getChildPositions(divideDeep, new Complex[] { A, B, C, D });
        graphicExec.execute(this);

        this.outputSpace = new OutputSpace(this.f);

        /** Output space frame */
        outFrame = new JFrame("Output space");
        outFrame.setSize(200, 250);
        outFrame.setResizable(false);
        outFrame.setLocationRelativeTo(null);
        outFrame.add(this.outputSpace);
        outFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("main.png")));

        for (int i = 0; i < childPosition.size(); i++) {
            System.out.println("rect:" + i);
            System.out.println(childPosition.get(i)[0]);
            System.out.println(childPosition.get(i)[1]);
            System.out.println(childPosition.get(i)[2]);
            System.out.println(childPosition.get(i)[3]);
        }
    }

    public void replay() {
        sq_points = new ArrayList<Complex>();
        A = new Complex(-range, -range);
        B = new Complex(range, -range);
        C = new Complex(range, range);
        D = new Complex(-range, range);
        deepCounter = 0;
        dir = Direction.RIGHT;
        newSquare = true;
        this.repaint();
    }

    public void addPoint(final Complex p) {
        sq_points.add(p);
    }

    ArrayList<Complex[]> getChildPositions(int deep, Complex[] parents) {
        ArrayList<Complex[]> tmpChilds = new ArrayList<Complex[]>();
        tmpChilds.add(parents);
        if (deep == 0) {
            return tmpChilds;
        }

        // remove first square
        if (deep == this.divideDeep) {
            tmpChilds = new ArrayList<Complex[]>();
        }

        Complex AB_mid = new Complex((parents[1].getRe() + parents[0].getRe()) / 2, parents[0].getIm());
        Complex BC_mid = new Complex(parents[1].getRe(), (parents[2].getIm() + parents[1].getIm()) / 2);
        Complex CD_mid = new Complex((parents[2].getRe() + parents[3].getRe()) / 2, parents[2].getIm());
        Complex AD_mid = new Complex(parents[3].getRe(), (parents[3].getIm() + parents[0].getIm()) / 2);
        Complex MIDDLE = new Complex((AB_mid.getRe() + CD_mid.getRe()) / 2, (AD_mid.getIm() + BC_mid.getIm()) / 2);

        Boolean rect1zero = new Solver(parents[0], AB_mid, MIDDLE, AD_mid, SolverAccuracy.LOW)
                .checkWindingNumber(this.f);
        ArrayList<Complex[]> rect1 = new ArrayList<Complex[]>();
        if (rect1zero)
            rect1 = getChildPositions(deep - 1, new Complex[] { parents[0], AB_mid, MIDDLE, AD_mid });

        Boolean rect2zero = new Solver(AB_mid, parents[1], BC_mid, MIDDLE, SolverAccuracy.LOW)
                .checkWindingNumber(this.f);
        ArrayList<Complex[]> rect2 = new ArrayList<Complex[]>();
        if (rect2zero)
            rect2 = getChildPositions(deep - 1, new Complex[] { AB_mid, parents[1], BC_mid, MIDDLE });

        Boolean rect3zero = new Solver(MIDDLE, BC_mid, parents[2], CD_mid, SolverAccuracy.LOW)
                .checkWindingNumber(this.f);
        ArrayList<Complex[]> rect3 = new ArrayList<Complex[]>();
        if (rect3zero)
            rect3 = getChildPositions(deep - 1, new Complex[] { MIDDLE, BC_mid, parents[2], CD_mid });

        Boolean rect4zero = new Solver(AD_mid, MIDDLE, CD_mid, parents[3], SolverAccuracy.LOW)
                .checkWindingNumber(this.f);
        ArrayList<Complex[]> rect4 = new ArrayList<Complex[]>();
        if (rect4zero)
            rect4 = getChildPositions(deep - 1, new Complex[] { AD_mid, MIDDLE, CD_mid, parents[3] });

        tmpChilds.addAll(rect1);
        tmpChilds.addAll(rect2);
        tmpChilds.addAll(rect3);
        tmpChilds.addAll(rect4);

        return tmpChilds;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 2, getHeight() / 2);

        if (!graphicTime.isRunning())
            graphicTime.start();

        if (panning) {
            graphicTime.stop();
            g2.setColor(Color.black);
            g2.drawLine(-getWidth() / 2 + originX, -getHeight() / 2 + originY, -getWidth() / 2 + originX - offX,
                    -getHeight() / 2 + originY - offY);
            g2.drawLine(-getWidth() / 2, -centerY - offY, getWidth() / 2, -centerY - offY);
            g2.drawLine(centerX - offX, -getHeight() / 2, centerX - offX, getHeight() / 2);
            return;
        }

        // draw rectangle points
        for (Complex p : sq_points) {
            Variable z0 = new Variable("z", p);
            Complex z = new Complex();
            try {
                z = Parser.eval(f, z0).getComplexValue();
            } catch (CalculatorException e) {
                z = new Complex(1000000, 1000000);
            }

            double fi;
            try {
                fi = Complex.phase(z) / 2 / Math.PI;
            } catch (Exception e) {
                fi = 0;
            }

            double r = Complex.abs(z);
            if (r > 1)
                r = 1;

            g2.setColor(Color.getHSBColor((float) fi, 1, (float) r));
            g2.fillRect((int) (p.getRe() * zoomX / scaleX) - 2 + centerX,
                    (int) (p.getIm() * zoomY / scaleY) - 2 - centerY, 4, 4);
        }

        // draw axes
        g2.setColor(Color.gray);
        g2.drawLine(-getWidth() / 2, -centerY, getWidth() / 2, -centerY);
        g2.drawLine(centerX, -getHeight() / 2, centerX, getHeight() / 2);

        // draw ticks
        g2.setColor(Color.gray);
        for (int i = -(int) ((getWidth() + 2 * centerX) / (2 * zoomX / scaleX))
                - 1; i < (int) ((getWidth() - 2 * centerX) / (2 * zoomX / scaleX)) + 1; i++) {
            g2.fillRect((int) (i * zoomX / scaleX) - 1 + centerX, -5 - centerY, 2, 10);
        }
        for (int i = -(int) ((getHeight() - 2 * centerY) / (2 * zoomY / scaleY))
                - 1; i < (int) ((getHeight() + 2 * centerY) / (2 * zoomY / scaleY)) + 1; i++) {
            g2.fillRect(-5 + centerX, (int) (i * zoomY / scaleY) - 1 - centerY, 10, 2);
        }

        repaint();
    }

    @Override
    public void run() {
        graphicTime = new Timer(speed, this);
    }

    public void update() {
        // add square points
        int sq_pts_len = sq_points.size();
        if (newSquare) {
            sq_points.add(A);
            newSquare = false;
        } else if (dir != Direction.STOP) {
            Complex last = sq_points.get(sq_pts_len - 1);
            double l_re = last.getRe();
            double l_im = last.getIm();

            if (dir == Direction.RIGHT) {
                Complex new_pt = new Complex(l_re + res, l_im);
                sq_points.add(new_pt);
                outputSpace.setCurrent(new_pt);
                if (new_pt.getRe() > B.getRe()) {
                    sq_points.add(B);
                    dir = Direction.DOWN;
                }
            } else if (dir == Direction.DOWN) {
                Complex new_pt = new Complex(l_re, l_im + res);
                sq_points.add(new_pt);
                outputSpace.setCurrent(new_pt);
                if (new_pt.getIm() > C.getIm()) {
                    sq_points.add(C);
                    dir = Direction.LEFT;
                }
            } else if (dir == Direction.LEFT) {
                Complex new_pt = new Complex(l_re - res, l_im);
                sq_points.add(new_pt);
                outputSpace.setCurrent(new_pt);
                if (new_pt.getRe() < D.getRe()) {
                    sq_points.add(D);
                    dir = Direction.UP;
                }
            } else if (dir == Direction.UP) {
                Complex new_pt = new Complex(l_re, l_im - res);
                sq_points.add(new_pt);
                outputSpace.setCurrent(new_pt);
                if (new_pt.getIm() < A.getIm()) {
                    sq_points.add(A);
                    dir = Direction.STOP;
                }
            }
        } else {
            if (deepCounter < childPosition.size()) {
                // sq_points = new ArrayList<Complex>();
                Complex[] nexSquare = childPosition.get(deepCounter);
                A = nexSquare[0];
                B = nexSquare[1];
                C = nexSquare[2];
                D = nexSquare[3];
                dir = Direction.RIGHT;
                deepCounter++;
                newSquare = true;
            } else {
                dir = Direction.STOP;
            }
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        originX = e.getX();
        originY = e.getY();
        panning = true;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (originX != -1 && originY != -1) {
            offX = originX - e.getX();
            offY = originY - e.getY();
            this.repaint();
        }
        this.repaint();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        centerX -= offX;
        centerY += offY;
        panning = false;
        this.repaint();
        originX = -1;
        originY = -1;
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        zoomX -= e.getPreciseWheelRotation() * scrollSpeed;
        zoomY -= e.getPreciseWheelRotation() * scrollSpeed;
        this.repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
    }

}
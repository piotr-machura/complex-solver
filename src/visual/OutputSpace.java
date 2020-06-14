package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import algorithm.parser.exception.CalculatorException;
import algorithm.parser.function.Complex;

/**
 * @Author Kacper Ledwosiński
 */
public class OutputSpace extends JPanel {
    private static final long serialVersionUID = 1L;

    float zoomX = 200, zoomY = 200;
    float scaleX = 4, scaleY = 4;
    String f;
    ArrayList<Complex> sq_points = new ArrayList<Complex>();

    Complex currentPoint = new Complex(0, 0);

    public OutputSpace(String f) {
        this.f = f;
    }

    public void addPoint(Complex p) {
        sq_points.add(p);
        this.repaint();
    }

    public void setCurrent(Complex c) {
        this.currentPoint = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 2, getHeight() / 2);

        double tickX = scaleX / zoomX;
        double tickY = scaleY / zoomY;

        /**
         * Paints output space with 1x1 pixel rectangles, color chosen as follows: hue =
         * phase, saturation = 1, brightness = radius
         */
        for (int j = -getHeight() / 2; j < getHeight() / 2; j++) {
            for (int i = -getWidth() / 2; i < getWidth() / 2; i++) {

                double x = i * tickX;
                double y = j * tickY;
                double fi;
                try {
                    fi = Complex.phase(new Complex(x, y)) / (2 * Math.PI);
                } catch (Exception e) {
                    fi = 0;
                }
                double r = Complex.abs(new Complex(x, y));
                if (r > 1) {
                    r = 1;
                }
                g2.setColor(Color.getHSBColor((float) fi, 1, (float) r));
                g2.fillRect(i, j, 1, 1);

            }
        }

        // draw current point
        double fi;
        try {
            fi = Complex.phase(currentPoint) / 2 / Math.PI;
        } catch (Exception e) {
            fi = 0;
        }

        double r = Complex.abs(currentPoint);
        if (r > 1)
            r = 1;

        g2.setColor(Color.white);
        int x;
        int y;
        try {
            double R = Complex.abs(currentPoint) * 10;
            x = (int) (R * Math.cos(Complex.phase(currentPoint)));
            y = (int) (R * Math.sin(Complex.phase(currentPoint)));
        } catch (CalculatorException e) {
            x = 0;
            y = 0;
        }
        g2.fillArc(x - 5, y - 5, 10, 10, 0, 360);

        g2.setColor(Color.gray);
        g2.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0);
        g2.drawLine(0, -getHeight() / 2, 0, getHeight() / 2);

        g2.setColor(Color.gray);
        for (int i = -(int) (getWidth() / (2 * zoomX / scaleX)) - 1; i < (int) (getWidth() / (2 * zoomX / scaleX))
                + 1; i++) {
            g2.fillRect((int) (i * zoomX / scaleX) - 1, -5, 2, 10);
        }
        for (int i = -(int) (getHeight() / (2 * zoomY / scaleY)) - 1; i < (int) (getHeight() / (2 * zoomY / scaleY))
                + 1; i++) {
            g2.fillRect(-5, (int) (i * zoomY / scaleY) - 1, 10, 2);
        }

        g2.setColor(Color.black);
        for (int i = 0; i < sq_points.size(); i++) {
            Complex p = sq_points.get(i);
            g2.fillRect((int) (p.getRe() * zoomY / scaleY) - 2, (int) (p.getIm() * zoomY / scaleY) - 2, 4, 4);
        }

    }

}
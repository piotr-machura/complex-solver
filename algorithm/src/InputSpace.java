package algorithm.src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InputSpace extends JPanel {
    private static final long serialVersionUID = 1L;

    float zoomX = 200, zoomY = 200;
    float scaleX = 4, scaleY = 4;
    Function f;
    ArrayList<Complex> sq_points = new ArrayList<Complex>();

    public InputSpace(Function f) {
        this.f = f;
    }

    public void addPoint(Complex p) {
        sq_points.add(p);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 2, getHeight() / 2);

        double tickX = scaleX / zoomX;
        double tickY = scaleY / zoomY;

        /*
         * Paints output space with small 1x1 pixel rectangles, color chosen as follows:
         * hue = phase f(z) , saturation = 1, brightness = raidus f(z)
         */
        for (int j = -getHeight() / 2; j < getHeight() / 2; j++) {
            for (int i = -getWidth() / 2; i < getWidth() / 2; i++) {

                double x = i * tickX;
                double y = j * tickY;
                Complex z = new Complex(x, y);

                z = f.solveFor(z);

                double fi = z.phase() / 2 / Math.PI;
                double r = z.abs();
                if (r > 1)
                    r = 1;

                g2.setColor(Color.getHSBColor((float) fi, 1, (float) r));
                g2.fillRect(i, j, 1, 1);

            }
        }

        // ? Co się tutaj dzieje

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
        for (Complex p : sq_points) {
            g2.fillRect((int) (p.re * zoomY / scaleY) - 2, (int) (p.im * zoomY / scaleY) - 2, 4, 4);
        }
    }

}
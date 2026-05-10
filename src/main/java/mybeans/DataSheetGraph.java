package mybeans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import javax.swing.JPanel;

public class DataSheetGraph extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private DataSheet dataSheet;
    private boolean connected;
    private int deltaX;
    private int deltaY;
    private int pointRadius;
    private Color color;

    public DataSheetGraph() {
        initialize();
    }

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
        repaint();
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        repaint();
    }

    public int getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = Math.max(1, deltaX);
        repaint();
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = Math.max(1, deltaY);
        repaint();
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(int pointRadius) {
        this.pointRadius = Math.max(2, pointRadius);
        repaint();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color == null ? Color.RED : color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            showGraph(graphics2D);
        } finally {
            graphics2D.dispose();
        }
    }

    public void showGraph(Graphics2D graphics) {
        double width = getWidth();
        double height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        Paint oldPaint = graphics.getPaint();
        Stroke oldStroke = graphics.getStroke();
        Font oldFont = graphics.getFont();

        graphics.setPaint(Color.WHITE);
        graphics.fill(new Rectangle2D.Double(0, 0, width, height));

        Bounds bounds = calculateBounds();
        double xScale = width / (bounds.getXMax() - bounds.getXMin());
        double yScale = height / (bounds.getYMax() - bounds.getYMin());
        double x0 = -bounds.getXMin() * xScale;
        double y0 = bounds.getYMax() * yScale;

        drawGrid(graphics, bounds, xScale, yScale, x0, y0, width, height);
        drawAxes(graphics, x0, y0, width, height);
        drawData(graphics, xScale, yScale, x0, y0);

        graphics.setPaint(oldPaint);
        graphics.setStroke(oldStroke);
        graphics.setFont(oldFont);
    }

    private void initialize() {
        connected = false;
        deltaX = 5;
        deltaY = 5;
        pointRadius = 6;
        color = Color.RED;
        setBackground(Color.WHITE);
        setSize(300, 400);
    }

    private Bounds calculateBounds() {
        double minX = 0.0;
        double maxX = 0.0;
        double minY = 0.0;
        double maxY = 0.0;

        if (dataSheet != null) {
            for (Data item : dataSheet.getDataItems()) {
                minX = Math.min(minX, item.getX());
                maxX = Math.max(maxX, item.getX());
                minY = Math.min(minY, item.getY());
                maxY = Math.max(maxY, item.getY());
            }
        }

        minX -= deltaX;
        maxX += deltaX;
        minY -= deltaY;
        maxY += deltaY;

        if (Double.compare(minX, maxX) == 0) {
            maxX = minX + 1;
        }
        if (Double.compare(minY, maxY) == 0) {
            maxY = minY + 1;
        }

        return new Bounds(minX, maxX, minY, maxY);
    }

    private void drawGrid(Graphics2D graphics, Bounds bounds, double xScale, double yScale,
            double x0, double y0, double width, double height) {
        float[] dashPattern = {8.0f, 8.0f};
        graphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
        graphics.setFont(new Font("Serif", Font.BOLD, 12));

        double xStep = calculateStep(bounds.getXMax() - bounds.getXMin());
        double firstX = Math.ceil(bounds.getXMin() / xStep) * xStep;
        for (double value = firstX; value <= bounds.getXMax(); value += xStep) {
            double x = x0 + value * xScale;
            graphics.setPaint(Color.LIGHT_GRAY);
            graphics.draw(new Line2D.Double(x, 0, x, height));
            if (Math.abs(value) > xStep / 100) {
                graphics.setPaint(Color.BLACK);
                graphics.drawString(formatTick(value), (int) x + 2, 14);
            }
        }

        double yStep = calculateStep(bounds.getYMax() - bounds.getYMin());
        double firstY = Math.ceil(bounds.getYMin() / yStep) * yStep;
        for (double value = firstY; value <= bounds.getYMax(); value += yStep) {
            double y = y0 - value * yScale;
            graphics.setPaint(Color.LIGHT_GRAY);
            graphics.draw(new Line2D.Double(0, y, width, y));
            if (Math.abs(value) > yStep / 100) {
                graphics.setPaint(Color.BLACK);
                graphics.drawString(formatTick(value), 4, (int) y - 3);
            }
        }
    }

    private void drawAxes(Graphics2D graphics, double x0, double y0, double width, double height) {
        graphics.setPaint(Color.BLACK);
        graphics.setStroke(new BasicStroke(2.0f));

        if (x0 >= 0 && x0 <= width) {
            graphics.draw(new Line2D.Double(x0, 0, x0, height));
            graphics.drawString("Y", (int) x0 + 4, 14);
        }
        if (y0 >= 0 && y0 <= height) {
            graphics.draw(new Line2D.Double(0, y0, width, y0));
            graphics.drawString("X", (int) width - 14, (int) y0 - 4);
        }
    }

    private void drawData(Graphics2D graphics, double xScale, double yScale, double x0, double y0) {
        if (dataSheet == null || dataSheet.isEmpty()) {
            return;
        }

        graphics.setPaint(color);
        graphics.setStroke(new BasicStroke(2.0f));

        double previousX = 0;
        double previousY = 0;
        boolean hasPrevious = false;

        for (Data item : dataSheet.getDataItems()) {
            double x = x0 + item.getX() * xScale;
            double y = y0 - item.getY() * yScale;

            if (connected && hasPrevious) {
                graphics.draw(new Line2D.Double(previousX, previousY, x, y));
            }

            double radius = pointRadius;
            graphics.setPaint(Color.WHITE);
            graphics.fill(new Ellipse2D.Double(x - radius / 2.0, y - radius / 2.0, radius, radius));
            graphics.setPaint(color);
            graphics.draw(new Ellipse2D.Double(x - radius / 2.0, y - radius / 2.0, radius, radius));

            previousX = x;
            previousY = y;
            hasPrevious = true;
        }
    }

    private static double calculateStep(double range) {
        double raw = Math.max(range / 10.0, 1.0);
        double magnitude = Math.pow(10.0, Math.floor(Math.log10(raw)));
        double normalized = raw / magnitude;

        if (normalized <= 1.0) {
            return magnitude;
        }
        if (normalized <= 2.0) {
            return 2.0 * magnitude;
        }
        if (normalized <= 5.0) {
            return 5.0 * magnitude;
        }
        return 10.0 * magnitude;
    }

    private static String formatTick(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.000001) {
            return Long.toString(Math.round(value));
        }
        return String.format("%.2f", value);
    }

    private static final class Bounds {
        private final double xMin;
        private final double xMax;
        private final double yMin;
        private final double yMax;

        private Bounds(double xMin, double xMax, double yMin, double yMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        private double getXMin() {
            return xMin;
        }

        private double getXMax() {
            return xMax;
        }

        private double getYMin() {
            return yMin;
        }

        private double getYMax() {
            return yMax;
        }
    }
}

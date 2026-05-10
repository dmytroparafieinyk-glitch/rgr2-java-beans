package mybeans;

import java.io.Serializable;
import java.util.Objects;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private double x;
    private double y;

    public Data() {
        this("", 0.0, 0.0);
    }

    public Data(String date, double x, double y) {
        this.date = date == null ? "" : date;
        this.x = x;
        this.y = y;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? "" : date;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Data{date='" + date + "', x=" + x + ", y=" + y + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Data)) {
            return false;
        }
        Data data = (Data) o;
        return Double.compare(data.x, x) == 0
                && Double.compare(data.y, y) == 0
                && Objects.equals(date, data.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, x, y);
    }
}

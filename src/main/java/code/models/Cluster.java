package code.models;

import java.util.ArrayList;
import java.util.List;

public class Cluster<T> {
    private final List<T> points;
    private final T center;

    public Cluster(final T center) {
        this.center = center;
        points = new ArrayList<T>();
    }


    public void addPoint(final T point) {
        points.add(point);
    }

    public List<T> getPoints() {
        return points;
    }

    public T getCenter() {
        return center;
    }
}

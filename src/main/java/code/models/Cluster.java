package code.models;

import java.util.ArrayList;
import java.util.List;

public class Cluster<T> {
    private static Integer clusterCount = 0;
    private final List<T> points;
    private final T center;
    private int clusterId;

    public Cluster(final T center) {
        synchronized (clusterCount){
            clusterCount += 1;
            this.clusterId = clusterCount;
        }
        this.center = center;
        points = new ArrayList<T>();
    }

    public int getClusterId() {
        return clusterId;
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

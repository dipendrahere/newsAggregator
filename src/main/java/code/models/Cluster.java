package code.models;

import code.databaseService.DBConnect;

import java.util.ArrayList;
import java.util.List;

public class Cluster<T> {
    private static Integer clusterCount = DBConnect.getInstance().maxClusterId();
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

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void addPoints(final List<T> points){
        this.points.addAll(points);
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
package code.clusteringComponent;

import java.util.*;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.Article;
import code.utility.GlobalFunctions;
import code.utility.Log;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.MathUtils;

public class DBScanClusterer<T extends Article> {
    private final double eps;
    private final int minPts;

    private enum PointStatus {
        NOISE,
        PART_OF_CLUSTER
    }
    public DBScanClusterer(final double eps, final int minPts)
            throws NotPositiveException {
        if (eps < 0.0d) {
            throw new NotPositiveException(eps);
        }
        if (minPts < 0) {
            throw new NotPositiveException(minPts);
        }
        this.eps = eps;
        this.minPts = minPts;
    }

    public double getEps() {
        return eps;
    }


    public int getMinPts() {
        return minPts;
    }

    public List<Cluster<T>> cluster(final Collection<T> points) throws NullArgumentException {

        // sanity checks
        MathUtils.checkNotNull(points);

        final List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
        final Map<T, PointStatus> visited = new HashMap<T, PointStatus>();

        for (final T point : points) {
            if (visited.get(point) != null) {
                continue;
            }
            final List<T> neighbors = getNeighbors(point, points);
            if (neighbors.size() >= minPts) {
                // DBSCAN does not care about center points
                final Cluster<T> cluster = new Cluster<T>(null);
                clusters.add(expandCluster(cluster, point, neighbors, points, visited));
            } else {
                visited.put(point, PointStatus.NOISE);
            }
        }

        return clusters;
    }

    private Cluster<T> expandCluster(final Cluster<T> cluster,
                                     final T point,
                                     final List<T> neighbors,
                                     final Collection<T> points,
                                     final Map<T, PointStatus> visited) {
        cluster.addPoint(point);
        visited.put(point, PointStatus.PART_OF_CLUSTER);

        List<T> seeds = new ArrayList<T>(neighbors);
        int index = 0;
        while (index < seeds.size()) {
            final T current = seeds.get(index);
            PointStatus pStatus = visited.get(current);
            // only check non-visited points
            if (pStatus == null) {
                final List<T> currentNeighbors = getNeighbors(current, points);
                if (currentNeighbors.size() >= minPts) {
                    seeds = merge(seeds, currentNeighbors);
                }
            }
            if (pStatus != PointStatus.PART_OF_CLUSTER) {
                visited.put(current, PointStatus.PART_OF_CLUSTER);
                cluster.addPoint(current);
            }

            index++;
        }
        return cluster;
    }

    private List<T> getNeighbors(final T point, final Collection<T> points) {
        final List<T> neighbors = new ArrayList<T>();
        for (final T neighbor : points) {
            try {
                if (point != neighbor && GlobalFunctions.similarty(neighbor, point) >= eps) {
                    neighbors.add(neighbor);
                }
            } catch (DissimilarArticleException e) {
                Log.error(e.getMessage());
            } catch (CategoryNotFoundException e) {
                Log.error(e.getMessage());
            }
        }
        return neighbors;
    }

    private List<T> merge(final List<T> one, final List<T> two) {
        final Set<T> oneSet = new HashSet<T>(one);
        for (T item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }
}


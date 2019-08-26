package code.clusteringComponent;

import java.util.*;
import java.util.stream.Collectors;

import code.databaseService.DBConnect;
import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.Article;
import code.models.Cluster;
import code.utility.GlobalFunctions;
import code.utility.Log;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.MathUtils;

public class DBScanClusterer<T extends Article> implements Clusterer<T>{
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

    public List<Cluster<T>> cluster(final List<T> points) throws NullArgumentException {

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

    @Override
    public HashMap<String, Integer> clusterIncrementally(HashMap<Article, Integer> points) throws NullArgumentException {
        return null;

    }

    /*public HashMap<String, Integer> clusterIncrementally(List<T> point) throws NullArgumentException {
        HashMap<String, Integer> newArticleMap = new HashMap<>();
        HashMap<T, Integer> articleClusterMap = (HashMap<T, Integer>) DBConnect.getInstance().articleClusterRelationship();
        List<T> total = new ArrayList<T>();
        for(T a: articleClusterMap.keySet()){
            total.add(a);
        }
        total.addAll(point);
        final Map<T, PointStatus> visited = new HashMap<T, PointStatus>();
        for (T l: point) {
            Article p = l;
            if(visited.get(point) != null){
                continue;
            }
            final List<T> neighbors = getNeighbors(l, total);
            HashMap<Integer, Integer> ccount = new HashMap<>();
            if(neighbors.size() >= minPts){
                boolean flag = true;
                for(T n: neighbors){
                    if(articleClusterMap.get(n) != null){
                        flag = false;
                        int clusterid = articleClusterMap.get(n);
                        if(ccount.containsKey(clusterid)){
                            ccount.put(clusterid, ccount.get(clusterid)+1);
                        }
                        else{
                            ccount.put(clusterid, 1);
                        }
                    }
                }
                if(flag){
                    Cluster<Article> c = new Cluster<>(null);
                    neighbors.stream().forEach(a -> {
                        c.addPoint(a);
                        visited.put(a, PointStatus.PART_OF_CLUSTER);
                        newArticleMap.put(a.getId(), c.getClusterId());
                    });
                }
                else{
                    final int[] cid = new int[1];
                    final int[] mx = {0};
                    ccount.keySet().forEach(a -> {
                        if(ccount.get(a) > mx[0]){
                            mx[0] = ccount.get(a);
                            cid[0] = a;
                        }
                    });
                    neighbors.stream().forEach(a -> {
                        visited.put(a, PointStatus.PART_OF_CLUSTER);
                        newArticleMap.put(a.getId(), cid[0]);
                    });
                }
            }

        }
        return newArticleMap;
    }*/

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
                if (point != neighbor && GlobalFunctions.cosineSimilarity(neighbor, point) >= eps) {
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


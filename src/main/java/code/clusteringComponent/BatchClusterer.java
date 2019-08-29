package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;

import java.util.List;

public class BatchClusterer<T extends Article> {
    private Clusterer<T> clusterer;
    public BatchClusterer(double eps, int minPts){
         clusterer = new DBScanClusterer<T>(eps, minPts);
    }
    public  List<Cluster<T>> cluster(final List<T> points){
        return clusterer.cluster(points);
    }
}

package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;

import java.util.List;

public class BatchClusterer<T extends Article> {
    private Clusterer<T> clusterer = new DBScanClusterer<T>(0.5, 4);
    public  List<Cluster<T>> cluster(final List<T> points){
        return clusterer.cluster(points);
    }
}

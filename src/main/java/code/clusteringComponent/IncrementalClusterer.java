package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;

import java.util.HashMap;
import java.util.List;

public class IncrementalClusterer <T extends Article> {
    private Clusterer<T> clusterer = new HierarchicalClusterer<>(0.4);
    public HashMap<String,Integer> clusterIncrementally(HashMap<Article,Integer> points){
        return clusterer.clusterIncrementally(points);
    }
}

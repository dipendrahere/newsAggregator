package code.clusteringComponent;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.models.Cluster;
import code.models.ClusterInfo;
import code.utility.Log;
import java.util.List;
import java.util.HashMap;

public class IncrementalClusterController {
    private CategoryType categoryType;
    private final double eps;
    public IncrementalClusterController(CategoryType categoryType, double eps){
        this.categoryType = categoryType;
        this.eps = eps;
    }
    public void run(){

        Log.debug("Incremental start for "+ categoryType);

        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship(categoryType);
        IncrementalClusterer<Article> hierarchicalClusterer = new IncrementalClusterer<Article>(eps);
        HashMap<String,Integer> updatedClusterId = hierarchicalClusterer.clusterIncrementally(hashMap);
        List<ClusterInfo> existingInfo = DBConnect.getInstance().getClusterInfo();
        List<ClusterInfo> info = new ClusterInfoHelper().incrementDiameters(updatedClusterId, hashMap, existingInfo);
        HashMap<String,Double> clusterRank = new ClusterInfoHelper().incrementalRanking(updatedClusterId,hashMap);
        DBConnect.getInstance().updateClusterIDs(updatedClusterId);
        DBConnect.getInstance().updateClusterInfo(info);
        DBConnect.getInstance().updateClusterRank(clusterRank);

        Log.debug("INCREMENTAL DONE for " + categoryType);

    }
}

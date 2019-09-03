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

        Log.debug("incremental Start for " + categoryType);
        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship(categoryType);
        Log.debug("debug 1 " + categoryType);
        IncrementalClusterer<Article> hierarchicalClusterer = new IncrementalClusterer<Article>(eps);
        HashMap<String,Integer> updatedClusterId = hierarchicalClusterer.clusterIncrementally(hashMap);
        Log.debug("debug 2 " + categoryType);
        List<ClusterInfo> existingInfo = DBConnect.getInstance().getClusterInfo();
        Log.debug("debug 3 " + categoryType);
        List<ClusterInfo> info = new ClusterInfoHelper().incrementDiameters(updatedClusterId, hashMap, existingInfo);
        Log.debug("debug 4 " + categoryType);
        HashMap<String,Integer> clusterRank = new ClusterInfoHelper().incrementalRanking(updatedClusterId,hashMap);
        Log.debug("debug 5 " + categoryType);
        DBConnect.getInstance().updateClusterRank(clusterRank);
        Log.debug("debug 6 " + categoryType);
        DBConnect.getInstance().updateClusterInfo(info);
        Log.debug("debug 7 " + categoryType);
        DBConnect.getInstance().updateClusterIDs(updatedClusterId);

        Log.debug("INCREMENTAL DONE for " + categoryType);
    }
}

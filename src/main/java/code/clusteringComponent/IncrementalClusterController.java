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
        try {
            HashMap<Article, Integer> hashMap = DBConnect.getInstance().articleClusterRelationship(categoryType);
            IncrementalClusterer<Article> hierarchicalClusterer = new IncrementalClusterer<Article>(eps);
            HashMap<String, Integer> updatedClusterId = hierarchicalClusterer.clusterIncrementally(hashMap);
            DBConnect.getInstance().updateClusterIDs(updatedClusterId);
//            Thread.sleep(100);
            List<ClusterInfo> existingInfo = DBConnect.getInstance().getClusterInfo();
            List<ClusterInfo> info = new ClusterInfoHelper().incrementDiameters(updatedClusterId, hashMap, existingInfo);
            HashMap<String, Double> clusterRank = new ClusterInfoHelper().incrementalRanking(updatedClusterId, hashMap);
            DBConnect.getInstance().updateClusterInfo(info);
//            Thread.sleep(100);
            DBConnect.getInstance().updateClusterRank(clusterRank);
//            Thread.sleep(100);
            Log.debug("INCREMENTAL DONE for " + categoryType);
        }
        catch (Exception e){
            Log.error("ERROR in incremental controller" + e.getMessage());
            e.printStackTrace();
        }
    }
}

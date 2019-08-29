package code.clusteringComponent;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.utility.Log;

import java.util.HashMap;

public class IncrementalClusterController {
    private CategoryType categoryType;
    private final double eps;
    public IncrementalClusterController(CategoryType categoryType, double eps){
        this.categoryType = categoryType;
        this.eps = eps;
    }
    public void run(){
        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship(categoryType);
        IncrementalClusterer<Article> hierarchicalClusterer = new IncrementalClusterer<Article>(eps);
        HashMap<String,Integer> updatedClusterId = hierarchicalClusterer.clusterIncrementally(hashMap);
        DBConnect.getInstance().updateClusterIDs(updatedClusterId);
        Log.debug("INCREMENTAL DONE");
    }
}

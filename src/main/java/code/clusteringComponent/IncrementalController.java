package code.clusteringComponent;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;

import java.util.HashMap;

public class IncrementalController {
    private CategoryType categoryType;
    public IncrementalController(CategoryType categoryType){
        this.categoryType = categoryType;
    }
    public void run(){
        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship(categoryType);
        Clusterer<Article> hierarchicalClusterer = new HierarchicalClusterer(0.45);
        HashMap<String,Integer> updatedClusterId = hierarchicalClusterer.clusterIncrementally(hashMap);
        DBConnect.getInstance().updateClusterIDs(updatedClusterId);
    }
}

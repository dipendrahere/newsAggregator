package code.clusteringComponent;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.models.Cluster;
import code.models.ClusterInfo;

import java.util.HashMap;
import java.util.List;

public class BatchClusterController {
    private CategoryType categoryType;

    public BatchClusterController(CategoryType categoryType){
        this.categoryType = categoryType;
    }

    public void startClustering(){
        List<Article> list = DBConnect.getInstance().fetchArticles(categoryType);
        BatchClusterer<Article> clusterer = new BatchClusterer<Article>();
        List<Cluster<Article>> clusters = clusterer.cluster(list);
        HashMap<String,Integer> hashMap = new HashMap<>();
        for(Cluster cluster : clusters){
            List<Article> articles = cluster.getPoints();
            for(Article article : articles){
                hashMap.put(article.getId(),cluster.getClusterId());
            }
        }
        DBConnect.getInstance().unassignClusters(categoryType);
        DBConnect.getInstance().updateClusterIDs(hashMap);

//       try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

package code;

import code.clusteringComponent.BatchClusterService;
import code.clusteringComponent.HierarchicalClusterer;
import code.clusteringComponent.IncrementalService;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;

import java.util.HashMap;
import java.util.List;


public class Main {
    public static void main(String[] args) {

    //    BatchClusterService.getInstance().start();
    //      PollingService.getInstance().poll();
        IncrementalService.getInstance().start();

        // Test for tf-idf function
//        List<Article> list = new ArrayList<>();
//
//        Article a = new ArticleBuilder("url4")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("i am vipin kumar u")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        Article b = new ArticleBuilder("url3")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("i am vipin kumar")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        List<Article> list = new ArrayList<>();
//        list.add(a);
//        list.add(b);
//
//        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship();
//        Iterator iterator = hashMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry mapElement = (Map.Entry)iterator.next();
//            Article c = (Article) mapElement.getKey();
//            System.out.println(c.getUrl() + " " + mapElement.getValue());
//
//        }


//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

//        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        System.out.println(list.size());
//        HierarchicalClusterer<Article> hierarchicalCluster = new HierarchicalClusterer<Article>(0.45);
//        hierarchicalCluster.cluster(list);
//        List<Cluster<Article>> clusters = hierarchicalCluster.cluster(list);
//        try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            Log.error("Unable to dump clusters");
//        }

//        HashMap<String,Integer> hashMap = new HashMap<>();
//        hashMap.put("0044625c8241a4728d35bd2b7402fb60",1);
//        hashMap.put("0049220afeb67fd2e2326b203450649b",5);
//        DBConnect.getInstance().updateClusterIDs(hashMap);

    }
}
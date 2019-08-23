package code;

import code.clusteringComponent.HierarchicalClusterer;
import code.contentComponent.PollingService;
import code.models.ArticleBuilder;
import code.models.Cluster;
import code.clusteringComponent.DBScanClusterer;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) {

//        PollingService.getInstance().poll();
        Article a = new ArticleBuilder("url4")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("i am vipin kumar u")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();

        Article b = new ArticleBuilder("url3")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("i am vipin kumar")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();

        List<Article> list = new ArrayList<>();
        list.add(a);
        list.add(b);

        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship();
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            Article c = (Article) mapElement.getKey();
            System.out.println(c.getUrl() + " " + mapElement.getValue());

        }


//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

//        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        System.out.println(list.size());
//        HierarchicalClusterer<Article> hierarchicalCluster = new HierarchicalClusterer<Article>(0.7);
//        List<Cluster<Article>> clusters = hierarchicalCluster.cluster(list);
//        try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            Log.error("Unable to dump clusters");
//        }

    }
}
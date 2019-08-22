package code;

import code.clusteringComponent.DataCleaner;
import code.clusteringComponent.HierarchicalCluster;
import code.clusteringComponent.PairComparator;
import code.contentComponent.PollingService;
import code.contentComponent.RssController;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import code.models.SportsTfIdfHelper;
import code.utility.GlobalFunctions;
import javafx.util.Pair;

import java.util.*;


public class Main {
    public static void main(String[] args) {

        //PollingService.getInstance().poll();
//        Article a = new ArticleBuilder("url4")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("i am vipin kumar")
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
//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
        System.out.println(list.size());
        HierarchicalCluster hierarchicalCluster = new HierarchicalCluster(list);
        hierarchicalCluster.performClustering();
        hierarchicalCluster.printClusters();


    }
}

package code;

import code.contentComponent.PollingService;
import code.models.Cluster;
import code.clusteringComponent.DBScanClusterer;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.utility.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {

//        System.out.println(DataCleaner.clean("Hello ' th'e of i demo's sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));

        //        System.out.println(DataCleaner.clean("Hello ' th'e of i sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        PollingService.getInstance().poll();

//        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        DBScanClusterer<Article> clusterer = new DBScanClusterer<>(0.6, 2);
//        int count = 0;
//        List<Cluster<Article>> clusters = clusterer.cluster(articles);


        /* Test for tf-idf function
        List<Article> list = new ArrayList<>();
        Article a = new ArticleBuilder("url4")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This is random shit")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        Article b = new ArticleBuilder("url5")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This 'is' Bull' shit")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        Article c = new ArticleBuilder("url3")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This is a great\" article")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        list.add(a);
        list.add(b);
        list.add(c);

         */
    }
}

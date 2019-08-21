package code;

import code.clusteringComponent.DataCleaner;
import code.contentComponent.PollingService;
import code.contentComponent.RssController;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        System.out.println(DataCleaner.clean("Hello ' th'e of i demo's sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
//        PollingService.getInstance().poll();

//        List<Article> list = new ArrayList<>();
//
//        Article a = new ArticleBuilder("url4")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("content")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        Article b = new ArticleBuilder("url5")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("content")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        list.add(a);
//        list.add(b);
//        DBConnect.getInstance().insertArticles(list);

    }
}

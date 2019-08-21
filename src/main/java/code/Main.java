package code;

import code.clusteringComponent.DataCleaner;
import code.contentComponent.PollingService;
import code.contentComponent.RssController;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import code.utility.GlobalFunctions;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //        System.out.println(DataCleaner.clean("Hello ' th'e of i sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
//        PollingService.getInstance().poll();

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

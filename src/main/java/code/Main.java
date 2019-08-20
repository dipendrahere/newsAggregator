package code;

import code.contentComponent.RssController;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        RssController rssController = new RssController();
        rssController.visitCategory("src/main/resources/rssSports.txt");

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

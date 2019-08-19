import DatabaseService.DBConnect;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import shared.Article;

import java.net.URL;
import java.util.Date;

public class Main {
    public static String extractFromUrl(URL url) throws BoilerpipeProcessingException {
        String output = ArticleExtractor.getInstance().getText(url);
        return output;
    }

    public static String extractFromHtml(String html) throws BoilerpipeProcessingException {
        String output = ArticleExtractor.getInstance().getText(html);
        return output;
    }
    public static void main(String[] args) {
        DBConnect dbConnect = new DBConnect();
        try {
            Article article = new Article(1,"demotitle",dbConnect.getCategoryId("sports"),"demoURL",new Date(),"demoContent","demoRss");
            dbConnect.insertArticle(article);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

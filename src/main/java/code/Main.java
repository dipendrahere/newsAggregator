package code;

import code.clusteringComponent.BatchClusterService;
import code.clusteringComponent.IncrementalClusterService;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.util.List;


public class Main {

    public static void updateImageUrls(){
        List<Article> list = DBConnect.getInstance().fetchAllArticles();
        for(Article article : list){
            Log.debug(article.getTitle());
            try {
                article.setImageUrl(GlobalFunctions.getImageFromUrl(article.getUrl()));
            }
            catch (Exception e){
                Log.debug("unable to find image Url of article " + e.getMessage());
            }
            DBConnect.getInstance().updateClusterImage(article);
        }
    }

    public static void main(String[] args) throws Exception {
//        String res = GlobalFunctions.extractFromUrl("https://www.nytimes.com/2019/08/30/business/dealbook/talking-ourselves-into-recession.html?emc=rss&partner=rss");
//        System.out.println(GlobalFunctions.extractFromHtml(res));
//        System.out.println(GlobalFunctions.getImageFromHTML(res));
        IncrementalClusterService.getInstance().start();
        BatchClusterService.getInstance().start();
        PollingService.getInstance().poll();
    }
}
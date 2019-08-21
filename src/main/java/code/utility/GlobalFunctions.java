package code.utility;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.models.WordAppearanceArticles;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import javafx.util.Pair;
import net.media.mnetcrawler.CrawlerConfig;
import net.media.mnetcrawler.DefaultProxyCrawlerConfig;
import net.media.mnetcrawler.SyncCrawler;
import net.media.mnetcrawler.bean.SyncCrawlResponse;
import net.media.mnetcrawler.util.RandomUserAgentManager;
import net.media.mnetcrawler.util.UserAgentManager;


import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlobalFunctions {


    public static String extractFromUrl(String url) throws Exception {
        UserAgentManager userAgentManager = new RandomUserAgentManager();
        CrawlerConfig crawlerConfig = new DefaultProxyCrawlerConfig("Taxonomy-Test", userAgentManager);
        SyncCrawlResponse response = null;
        try {
            SyncCrawler syncCrawler = new SyncCrawler(crawlerConfig);
            response = syncCrawler.crawl(url);
        }catch(Exception e){
            Log.error("Caught an exception while trying to crawl :: " +e.getMessage());
        }
        Log.debug("crawl :: status code string: "+ response.getStatusString() + " :: status code: " +response.getStatusCode());
        return extractFromHtml(response.getContent());
    }

    public static String extractFromUrl(URL url) throws BoilerpipeProcessingException {
        return ArticleExtractor.getInstance().getText(url);
    }

    public static String extractFromHtml(String html) throws BoilerpipeProcessingException {
        return ArticleExtractor.getInstance().getText(html);
    }

    public static String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String,Double> tfIdf(String article){
        HashMap<String,Double> tf= new HashMap<>();
        String[] words = article.split(" ");
        for(String word : words){
            if(tf.containsKey(word)){
                tf.put(word,tf.get(word)+1);
            }
            else{
                tf.put(word,1.0);
            }
        }
        int size = WordAppearanceArticles.getInstance().getSize();

        Iterator hmIterator = tf.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            double normaliseValue = ((double)mapElement.getValue()/words.length);
            int frequency = WordAppearanceArticles.getInstance().getFrequencyOfWorld((String)mapElement.getKey());
            double idf = Math.log(size/frequency);
            tf.put((String)mapElement.getKey(),normaliseValue*idf);
        }
        return tf;
    }


    /*
        Return: returns a hashmap that contains word appearance in all articles and total number of articles
    */
    public static Pair<HashMap<String,Integer>,Integer> idf(){
        HashMap<String,Integer> ret = new HashMap<>();
        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.SPORTS);
        for(Article article:articles){
            String content = article.getContent();
            String[] words = content.split(" ");
            HashMap<String,Boolean> temp = new HashMap<>();
            for(String word : words){
                if(!temp.containsKey(word)){
                    temp.put(word,true);
                }
            }
            Iterator hmIterator = temp.entrySet().iterator();
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                if(ret.containsKey(mapElement.getKey())){
                    ret.put((String)mapElement.getKey(),ret.get(mapElement.getKey())+1);
                }
                else{
                    ret.put((String)mapElement.getKey(),1);
                }
            }
        }
        return new Pair(ret,articles.size());
    }

}

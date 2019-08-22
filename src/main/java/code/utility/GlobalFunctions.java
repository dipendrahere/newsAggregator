package code.utility;

import code.databaseService.DBConnect;
import code.exceptions.DissimilarArticleException;
import code.models.*;
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
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    public static double Mod(HashMap<String,Double> hm){
        double ret = 0.0;
        Iterator hmIterator = hm.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            ret += (((double)mapElement.getValue()) * ((double)mapElement.getValue()));
        }
        return Math.sqrt(ret);
    }

    public static Double cosineDissimilarity(Article a, Article b) throws DissimilarArticleException{
        double dissimilarity = 1 - cosineSimilarity(a, b);
        return dissimilarity > 0 ? dissimilarity : 0;
    }
    public static Double cosineSimilarity(Article a, Article b) throws DissimilarArticleException {
        if(a.getCategoryType() != b.getCategoryType()){
            throw new DissimilarArticleException();
        }
        TfIdfHelper helper;
        switch (a.getCategoryType()){
            case SPORTS:
                helper = SportsTfIdfHelper.getInstance();
                break;
            case WORLD:
                helper = WorldTfIdfHelper.getInstance();
                break;
            case SCITECH:
                helper = SciTechTfIdfHelper.getInstance();
                break;
            case BUSINESS:
                helper = BusinessTfidfHelper.getInstance();
                break;
            default:
                throw new DissimilarArticleException();
        }
        double ret = 0;
        HashMap<String,Double> hm1 = helper.tfIdf(a);
        HashMap<String,Double> hm2 = helper.tfIdf(b);
        Iterator hmIterator = hm1.entrySet().iterator();
        double dotProduct = 0.0;
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            if(hm2.containsKey(mapElement.getKey())){
                dotProduct += (((double)mapElement.getValue()) * hm2.get(mapElement.getKey()));
            }
        }
        ret = dotProduct/(Mod(hm1)*Mod(hm2));
        return ret;
    }
    // Todo : Remove this function after testing of idf
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

    // Function to sort the Hashmap in assending order
    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}

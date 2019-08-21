package code.utility;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.*;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
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
import java.util.HashMap;
import java.util.Iterator;
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
    public static double Mod(HashMap<String,Double> hm){
        double ret = 0.0;
        Iterator hmIterator = hm.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            ret += (((double)mapElement.getValue()) * ((double)mapElement.getValue()));
        }
        return Math.sqrt(ret);
    }
    public static Double similarty(Article a, Article b) throws DissimilarArticleException {
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
            dotProduct += (((double)mapElement.getValue()) * hm2.get(mapElement.getKey()));
        }
        ret = dotProduct/(Mod(hm1)*Mod(hm2));
        return ret;
    }
}

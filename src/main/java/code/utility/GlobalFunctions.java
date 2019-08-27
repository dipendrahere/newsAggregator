package code.utility;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.idfHelper.*;
import code.models.Article;
import code.models.Cluster;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import net.media.mnetcrawler.CrawlerConfig;
import net.media.mnetcrawler.DefaultProxyCrawlerConfig;
import net.media.mnetcrawler.SyncCrawler;
import net.media.mnetcrawler.bean.SyncCrawlResponse;
import net.media.mnetcrawler.util.RandomUserAgentManager;
import net.media.mnetcrawler.util.UserAgentManager;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


    public static Double cosineDissimilarity(Article a, Article b) throws DissimilarArticleException, CategoryNotFoundException{
        double dissimilarity = 1 - cosineSimilarity(a, b);
        return dissimilarity > 0 ? dissimilarity : 0;
    }

    public static Double cosineSimilarity(Article a, Article b) throws DissimilarArticleException, CategoryNotFoundException {
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
                throw new CategoryNotFoundException();
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
        try {
            ret = dotProduct / (Mod(hm1) * Mod(hm2));
        }
        catch (Exception e){
            ret = 0;
        }
        return ret;
    }

    // Function to sort the Hashmap in assending order
    public static HashMap<String, Double> sort(HashMap<String, Double> hm)
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


    public static void deleteFile(File file)
            throws IOException{
        if(file.isDirectory()){
            if(file.list().length==0){
                file.delete();
            }else{
                String files[] = file.list();
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    deleteFile(fileDelete);
                }
                if(file.list().length==0){
                    file.delete();
                }
            }
        }else{
            file.delete();
        }
    }

    public static void dumpClusters(List<Cluster<Article>> clusters) throws IOException {
        int count = 1;
        File directory = new File("clusters");
        if(!directory.exists()){
            System.out.println("Directory does not exist.");
        }else{
            try{
                deleteFile(directory);

            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        File d = new File("clusters");
        d.mkdir();
        System.out.println("Total Clusters: "+clusters.size());
        for(Cluster<Article> cluster: clusters){
            File file = new File("clusters/"+cluster.getClusterId());
            count++;
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            FileWriter writer = new FileWriter(file);
          for(Article a : cluster.getPoints()){
                writer.write(a.toString());
            }
            writer.close();
        }
    }
}

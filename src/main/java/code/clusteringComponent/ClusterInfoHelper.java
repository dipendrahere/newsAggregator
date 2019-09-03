package code.clusteringComponent;

import code.models.*;
import code.utility.GlobalFunctions;
import code.utility.Log;
import code.utility.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;

public class ClusterInfoHelper {

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



    public List<ClusterInfo> batchInformation(List<Cluster<Article>> clusters){
        return clusters.stream().map(cluster -> {
            ClusterInfo info  = new ClusterInfo();
            info.setClusterId(cluster.getClusterId());
            Set<String> distinctRss = cluster.getPoints().stream().map(a -> a.getRssLink()).collect(Collectors.toSet());
            Stream<Date> dates = cluster.getPoints().stream().map(a -> a.getPublishedDate());
            Date mostRecent = cluster.getPoints().stream().map(a -> a.getPublishedDate()).reduce((a, b)-> a.compareTo(b) > 0 ? a:b).get();
            int count = cluster.getPoints().size();
            Date averageDate = dates.reduce((a, b) -> {
                long ad = a.getTime();
                long bd = b.getTime();
                return new Date(ad + bd);
            }).get();
            info.setDistinctRss(distinctRss);
            info.setRecency(mostRecent);
            info.setAverageDate(averageDate);
            info.setTotalPoints(count);
            info.setDiameter(calculateDiameter(cluster.getPoints()));
            return info;
        }).collect(Collectors.toList());
    }


    private double calculateDiameter(List<Article> articles){
        Double maxi = 0.0;
        for(int i=0;i<articles.size();i++){
            for(int j=i+1;j<articles.size();j++){
                try {
                    maxi = Math.max(maxi, GlobalFunctions.cosineDissimilarity(articles.get(i),articles.get(j)));
                }
                catch (Exception e){
                    Log.error("unable to find cosine dissimilarty " + e.getMessage());
                }
            }
        }
        return maxi;
    }


    public List<ClusterInfo> incrementDiameters(HashMap<String,Integer> assignedCluster, HashMap<Article,Integer> hashMap, List<ClusterInfo> clusterInfos){


        HashMap<Integer,ClusterInfo> clusterInfoHashMap = new HashMap<>();
        for(ClusterInfo clusterInfo : clusterInfos){
            clusterInfoHashMap.put(clusterInfo.getClusterId(),clusterInfo);
        }

        HashMap<Integer,List<Article>> hmap = new HashMap<>();
        List<Article> NonClusteredArticles = new ArrayList<>();
        Iterator iterator = hashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            if((Integer)mapElement.getValue() == 0){
                NonClusteredArticles.add((Article)mapElement.getKey());
                continue;
            }
            if(hmap.containsKey(mapElement.getValue())){
                hmap.get(mapElement.getValue()).add((Article)mapElement.getKey());
            }
            else{
                List<Article> list = new ArrayList<>();
                list.add((Article)mapElement.getKey());
                hmap.put((Integer)mapElement.getValue(),list);
            }
        }

        List<ClusterInfo> ret = new ArrayList<>();

        HashMap<Integer,List<Article>> newClusters = new HashMap<>();

        HashMap<Integer,ClusterInfo> retHashmap = new HashMap<>();

        for(Article article : NonClusteredArticles){
            int assignedClusterNo = assignedCluster.get(article.getId());
            if(hmap.containsKey(assignedClusterNo)){
                Double maxi = 0.0;
                List<Article> alreadyArticlesinClusters = hmap.get(assignedClusterNo);
                for (Article already : alreadyArticlesinClusters){
                    try {
                        maxi = Math.max(maxi,GlobalFunctions.cosineDissimilarity(already,article));
                    }
                    catch (Exception e){
                        Log.error("unable to find cosine dissimilarty "+ e.getMessage());
                    }
                }
                ClusterInfo clusterInfo;
                if(retHashmap.containsKey(assignedClusterNo)){
                    clusterInfo = retHashmap.get(assignedClusterNo);
                }
                else{
                    clusterInfo = clusterInfoHashMap.get(assignedClusterNo);
                }
                clusterInfo.setTotalPoints(clusterInfo.getTotalPoints()+1);
                clusterInfo.setDiameter(Math.max(maxi,clusterInfo.getDiameter()));
                Date newAvgDate = new Date(clusterInfo.getAverageDate().getTime() + article.getPublishedDate().getTime());
                clusterInfo.setAverageDate(newAvgDate);
                clusterInfo.addRssLink(article.getRssLink());
                if(clusterInfo.getRecency().compareTo(article.getPublishedDate()) < 0){
                    clusterInfo.setRecency(article.getPublishedDate());
                }
                retHashmap.put(assignedClusterNo,clusterInfo);
            }
            else{
                if(newClusters.containsKey(assignedClusterNo)){
                    newClusters.get(assignedClusterNo).add(article);
                }
                else{
                    List<Article> temp = new ArrayList<>();
                    temp.add(article);
                    newClusters.put(assignedClusterNo,temp);
                }
            }
        }
        List<Cluster<Article>> newClusterList = newClusters.keySet().stream().map(key -> {
                Cluster<Article> c = new Cluster<>(null);
                c.setClusterId(key);
                c.addPoints(newClusters.get(key));
                return c;
        }).collect(Collectors.toList());
        List<ClusterInfo> infos = batchInformation(newClusterList);
        ret.addAll(infos);

        iterator = retHashmap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            ret.add((ClusterInfo)mapElement.getValue());
        }
        return ret;
    }

    public HashMap<String,Integer> batchRanking(List<Cluster<Article>> clusters){
        HashMap<String,Integer> ret = new HashMap<>();
        for(Cluster cluster : clusters){
            List<Article> articles = cluster.getPoints();
            HashMap<String,Double> temp = new HashMap<>();
            String allContent = "";
            CategoryType categoryType = CategoryType.SPORTS;
            for (Article article : articles){
                allContent += article.getContent();
                allContent += " ";
                categoryType = article.getCategoryType();
            }
            Article major = new ArticleBuilder("demo")
                    .setContent(allContent)
                    .setTitle("demo")
                    .setCategoryType(categoryType)
                    .build();
            for(Article article : articles){
                try {
                    temp.put(article.getId(),GlobalFunctions.cosineDissimilarity(major,article));
                }
                catch (Exception e){
                    Log.error("unable to find cosine dissimilarty "+ e.getMessage());
                }
            }
            HashMap<String,Double> sorted = sortByValue(temp);

            Iterator iterator = sorted.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()){
                Map.Entry mapElement = (Map.Entry)iterator.next();
                ret.put((String)mapElement.getKey(),i);
                i++;
            }

        }
        return ret;
    }

    public HashMap<String,Integer> incrementalRanking(HashMap<String,Integer> assignedCluster, HashMap<Article,Integer> hashMap){
        HashMap<Integer,List<Article>> hmap = new HashMap<>();
        List<Article> NonClusteredArticles = new ArrayList<>();
        Iterator iterator = hashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            if((Integer)mapElement.getValue() == 0){
                NonClusteredArticles.add((Article)mapElement.getKey());
                continue;
            }
            if(hmap.containsKey(mapElement.getValue())){
                hmap.get(mapElement.getValue()).add((Article)mapElement.getKey());
            }
            else{
                List<Article> list = new ArrayList<>();
                list.add((Article)mapElement.getKey());
                hmap.put((Integer)mapElement.getValue(),list);
            }
        }
        List<Cluster<Article>> ret = new ArrayList<>();
        HashMap<Integer,List<Article>> newClusters = new HashMap<>();
        for(Article nonArticle : NonClusteredArticles){
            int cluster_id = assignedCluster.get(nonArticle.getId());
            if(hmap.containsKey(cluster_id)){
                List<Article> temp = hmap.get(cluster_id);
                hmap.get(cluster_id).clear();
                temp.add(nonArticle);
                if(!newClusters.containsKey(cluster_id)){
                    newClusters.put(cluster_id,temp);
                }
                else{
                    newClusters.get(cluster_id).add(nonArticle);
                }
            }
            else{
                if(!newClusters.containsKey(cluster_id)){
                    List<Article> temp = new ArrayList<>();
                    temp.add(nonArticle);
                    newClusters.put(cluster_id,temp);
                }
                else{
                    newClusters.get(cluster_id).add(nonArticle);
                }
            }
        }
        iterator = newClusters.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            Cluster<Article> cluster = new Cluster<>(null);
            cluster.addPoints((List<Article>) mapElement.getValue());
            ret.add(cluster);
        }
        return batchRanking(ret);
    }

}

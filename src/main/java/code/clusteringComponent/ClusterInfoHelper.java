package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;
import code.models.ClusterInfo;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterInfoHelper {

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


    public double calculateDiameter(List<Article> articles){
//        return 0;
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

}

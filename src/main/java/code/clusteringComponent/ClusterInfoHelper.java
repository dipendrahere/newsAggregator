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
            Date mostRecent = dates.reduce((a, b)-> a.compareTo(b) > 0 ? a:b).get();
            int count = cluster.getPoints().size();
            Date averageDate = dates.reduce((a, b) -> {
                long ad = a.getTime();
                long bd = b.getTime();
                return new Date(ad + bd);
            }).get();
            averageDate = new Date(averageDate.getTime() / count);
            info.setCoverageScore(distinctRss.size());
            info.setRecency(mostRecent);
            info.setAverageDate(averageDate);
            info.setTotalPoints(count);
            info.setDiameter(calculateDiameter(cluster.getPoints()));
            return info;
        }).collect(Collectors.toList());
    }


    public double calculateDiameter(List<Article> articles){
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
                    clusterInfo = ret.get(assignedClusterNo);
                }
                else{
                    clusterInfo = clusterInfoHashMap.get(assignedClusterNo);
                }
                clusterInfo.setTotalPoints(clusterInfo.getTotalPoints()+1);
                clusterInfo.setDiameter(Math.max(maxi,clusterInfo.getDiameter()));
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

        iterator = newClusters.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            ClusterInfo clusterInfo = new ClusterInfo();
            clusterInfo.setClusterId((Integer)mapElement.getKey());
            clusterInfo.setDiameter(calculateDiameter((List<Article>)mapElement.getValue()));
            clusterInfo.setTotalPoints(((List<Article>) mapElement.getValue()).size());
            ret.add(clusterInfo);
        }

        iterator = retHashmap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            ret.add((ClusterInfo)mapElement.getValue());
        }
        return ret;
    }

}

package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;
import code.models.ClusterInfo;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterInfoHelper {
    public List<ClusterInfo> coverageRecencyAndCount(List<Cluster<Article>> clusters){
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
}

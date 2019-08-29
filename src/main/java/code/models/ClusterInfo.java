package code.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClusterInfo {
    private int clusterId;
    private Date recency;
    private int totalPoints;
    private Date averageDate;
    private Double diameter;
    private Set<String> distinctRss;

    public ClusterInfo(){
        distinctRss = new HashSet<>();
    }

    public void addRssLinks(List<String> rssList){
        distinctRss.addAll(rssList);
    }

    public void addRssLink(String rss){
        distinctRss.add(rss);
    }
    public Set<String> getDistinctRss() {
        return distinctRss;
    }

    public void setDistinctRss(Set<String> distinctRss) {
        this.distinctRss = distinctRss;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public int getClusterId() {
        return clusterId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Date getAverageDate() {
        return averageDate;
    }

    public void setAverageDate(Date averageDate) {
        this.averageDate = averageDate;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public Date getRecency() {
        return recency;
    }

    public void setRecency(Date recency) {
        this.recency = recency;
    }

}

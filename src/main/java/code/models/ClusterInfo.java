package code.models;

import java.util.Date;

public class ClusterInfo {
    private int clusterId;
    private double coverageScore;
    private Date recency;
    private int totalPoints;
    private Date averageDate;
    private Double diameter;

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

    public double getCoverageScore() {
        return coverageScore;
    }

    public void setCoverageScore(double coverageScore) {
        this.coverageScore = coverageScore;
    }

    public Date getRecency() {
        return recency;
    }

    public void setRecency(Date recency) {
        this.recency = recency;
    }

}

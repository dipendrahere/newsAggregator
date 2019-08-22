package code.clusteringComponent;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.Article;
import code.utility.GlobalFunctions;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HierarchicalCluster {
    public List<Article> articles;
    public int n;
    public double[][] distanceMatrix;
    List<PriorityQueue< Pair<Double,Integer> > >pq = new ArrayList<>();
    public int[] dsu;
    public int[] dsuSize;
    public double eps = 0.7;
    public int getParent(int k){
        while (k != dsu[k]){
            k = dsu[k];
        }
        return k;
    }

    public void merge(int i,int j){
        int p = getParent(i);
        int q = getParent(j);
        if(dsuSize[p] >= dsuSize[q]){
            dsuSize[p] += dsuSize[q];
            dsu[q] = p;
        }
        else{
            dsuSize[q] += p;
            dsu[p] = q;
        }
    }


    public HierarchicalCluster(List<Article> articles){
        this.articles = articles;
        n = articles.size();
        distanceMatrix = new double[n][n];
        dsu = new int[n];
        dsuSize = new int[n];
        for(int i=0;i<n;i++){
            dsu[i] = i;
            dsuSize[i] = 1;
            pq.add(new PriorityQueue<>(n,new PairComparator()));
        }
    }
    public void calculateDistanceMatrix() throws DissimilarArticleException, CategoryNotFoundException {
        for(int i=0;i<articles.size();i++){
            for(int j=0;j<=i;j++){
                if(i == j){
                    distanceMatrix[i][j] = 10;
                    continue;
                }
                distanceMatrix[i][j] = GlobalFunctions.cosineDissimilarity(articles.get(i),articles.get(j));
                distanceMatrix[j][i] = distanceMatrix[i][j];
            }
        }
    }

    public void initPriorityQueues(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i == j){
                    continue;
                }
                pq.get(i).add(new Pair<>(distanceMatrix[i][j],j));
            }
        }
    }

    public void performClustering(){
        try {
            calculateDistanceMatrix();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        initPriorityQueues();
        int count = 0;
        while(count < n-1){
            double mini = 10;
            int index = -1;
            Pair<Double,Integer> temp = new Pair<>(0.0,0);
            for(int i=0;i<n;i++){
                if(pq.get(i).peek().getKey() < mini){
                    mini = pq.get(i).peek().getKey();
                    temp = pq.get(i).peek();
                    index = i;
                }
            }
            if(temp.getKey() > eps){
                break;
            }
            merge(index,temp.getValue());
            pq.get(index).poll();
            pq.get(temp.getValue()).poll();
            count++;
        }
    }

    public void printLargestCluster(){
        int maxi = 0;
        int index = -1;
        for(int i=0;i<n;i++){
            if(dsuSize[i] > maxi){
                maxi = dsuSize[i];
                index = i;
            }
        }
        for(int i=0;i<n;i++){
            if(dsu[i] == index){
                System.out.println(articles.get(i).getUrl());
            }
        }
    }

    public void printDistMat(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }


}

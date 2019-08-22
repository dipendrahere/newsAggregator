package code.clusteringComponent;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.Article;
import code.models.Cluster;
import code.utility.GlobalFunctions;
import javafx.util.Pair;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

import java.util.*;

public class HierarchicalClusterer<T extends Article> implements Clusterer<T>{

    final private class PairComparator implements Comparator<Pair<Double,Integer>> {
        @Override
        public int compare(Pair<Double, Integer> o1, Pair<Double, Integer> o2) {
            if(o1.getKey() > o2.getKey()){
                return 1;
            }
            else if(o1.getKey() < o2.getKey()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    private List<T> articles;
    private int n;
    private double[][] distanceMatrix;
    private List<PriorityQueue< Pair<Double,Integer> > >pq = new ArrayList<>();
    private int[] dsu;
    private int[] dsuSize;
    private double eps;
    private int getParent(int k){
        while (k != dsu[k]){
            k = dsu[k];
        }
        return k;
    }

    private void merge(int i,int j){
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

    public HierarchicalClusterer(double eps){
        this.eps = eps;
    }

    public List<Cluster<T>> cluster(List<T> articles) throws NullArgumentException {
        MathUtils.checkNotNull(articles);
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
        return new ArrayList<>();
    }


    private void calculateDistanceMatrix() throws DissimilarArticleException, CategoryNotFoundException {
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

    private void initPriorityQueues(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i == j){
                    continue;
                }
                pq.get(i).add(new Pair<>(distanceMatrix[i][j],j));
            }
        }
    }

    private void performClustering(){
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

    private void printLargestCluster(){
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

    private void printDistMat(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }


}

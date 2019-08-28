package code.clusteringComponent;

import code.exceptions.CategoryNotFoundException;
import code.exceptions.DissimilarArticleException;
import code.models.Article;
import code.models.Cluster;
import code.utility.GlobalFunctions;
import code.utility.Pair;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import java.util.*;

public class HierarchicalClusterer<T extends Article> implements Clusterer<T>{

    final private class PairComparatorAsc implements Comparator<Pair<Double,Integer>> {
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

    final private class PairComparatorDesc implements Comparator<Pair<Integer,Integer>> {
        @Override
        public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
            if(o1.getKey() < o2.getKey()){
                return 1;
            }
            else if(o1.getKey() > o2.getKey()){
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
        if(p == q){
            return;
        }
        if(dsuSize[p] >= dsuSize[q]){
            dsuSize[p] += dsuSize[q];
            dsu[q] = p;
        }
        else{
            dsuSize[q] += dsuSize[p];
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
            pq.add(new PriorityQueue<>(n,new PairComparatorAsc()));
        }
        return performClustering();
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

    private List<Cluster<T>> performClustering(){
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
//        printClusters();
//        return null;
        return getAllClusters();
    }


    public HashMap<String,Integer> clusterIncrementally(HashMap<Article,Integer> hashMap){
        HashMap<Integer,List<Article>> hmap = new HashMap<>();
        HashMap<String,Integer> ret = new HashMap<>();
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
        for(Article article : NonClusteredArticles){
            double mini = 10;
            int ans = 0;
            iterator = hmap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry mapElement = (Map.Entry)iterator.next();
                double minimumInCluster = 10;
                List<Article> articles = (List<Article>)mapElement.getValue();
                for(Article a : articles){
                    try {
                        minimumInCluster = Math.min(GlobalFunctions.cosineDissimilarity(a,article),minimumInCluster);
                    }
                    catch (Exception e){
                        e.printStackTrace();;
                    }

                }
                if(mini > minimumInCluster){
                    mini = minimumInCluster;
                    ans = (Integer)mapElement.getKey();
                }
            }
            if(mini > eps){
                ans = 0;
            }
            if(ans == 0){
                List<Article> temp = new ArrayList<>();
                temp.add(article);
                Cluster<Article> newCluster = new Cluster<>(null);
                temp.forEach(a -> newCluster.addPoint(a));
                hmap.put(newCluster.getClusterId(),temp);
                ret.put(article.getId(),newCluster.getClusterId());
            }
            else {
                hmap.get(ans).add(article);
                ret.put(article.getId(), ans);

            }
        }
        return ret;
    }

    private List<Cluster<T>> getAllClusters(){
        List<Cluster<T>> clusters = new ArrayList<>();
        List<Pair<Integer,Integer>> list = new ArrayList<>();
        for(int i=0;i<n;i++){
            if(getParent(i) == i){
                list.add(new Pair<>(dsuSize[i],i));
            }
        }
        list.sort(new PairComparatorDesc());
        for(int i=0;i < list.size(); i++){
            Cluster<T> cluster = new Cluster<>(null);
            for(int j=0;j<n;j++){
                if(getParent(dsu[j]) == list.get(i).getValue()){
                    cluster.addPoint(articles.get(j));
                //    System.out.println(articles.get(j).getTitle()+ "  " + articles.get(j).getUrl());
                }
            }
            clusters.add(cluster);
        }
        return clusters;
    }

    // Todo Remove this function after testing
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
            if(getParent(dsu[i]) == index){
                System.out.println(articles.get(i).getTitle());
            }
        }
    }

    // Todo Remove this function after testing
    public void printClusters(){
        List<Pair<Integer,Integer>> list = new ArrayList<>();
        for(int i=0;i<n;i++){
            if(getParent(i) == i){
                list.add(new Pair<>(dsuSize[i],i));
            }
        }
        list.sort(new PairComparatorDesc());
        for(int i=0;i<Math.min(10,list.size());i++){
            System.out.println(list.get(i).getKey() + " " + list.get(i).getValue());
            for(int j=0;j<n;j++){
                if(getParent(dsu[j]) == list.get(i).getValue()){
                    System.out.println(articles.get(j).getTitle()+ "  " + articles.get(j).getUrl());
                }
            }
            System.out.println();
            System.out.println();
        }
    }

    // Todo Remove this function after testing
    private void printDistMat(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}

package code.clusteringComponent;

import javafx.util.Pair;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<Double,Integer>> {
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

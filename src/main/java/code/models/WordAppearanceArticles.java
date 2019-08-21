package code.models;

import code.utility.GlobalFunctions;
import javafx.util.Pair;

import java.util.HashMap;

public class WordAppearanceArticles {
    HashMap<String,Integer> hm;
    private static WordAppearanceArticles wordAppearanceArticles;
    int size;
    public static WordAppearanceArticles getInstance(){
        if (wordAppearanceArticles==null)
            wordAppearanceArticles = new WordAppearanceArticles();
        return wordAppearanceArticles;
    }
    private WordAppearanceArticles(){
        Pair<HashMap<String,Integer>,Integer> temp = GlobalFunctions.idf();
        this.hm = temp.getKey();
        this.size = temp.getValue();
    }
    public int getSize(){
        return size;
    }
    public int getFrequencyOfWorld(String word){
        if(hm.containsKey(word)){
            return hm.get(word);
        }
        else{
            return 0;
        }
    }
}

package code.idfHelper;

import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.utility.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class TfIdfHelper {
    volatile private HashMap<String,Integer> hm;
    volatile private int size;
    private CategoryType categoryType;

    public TfIdfHelper(CategoryType type){
        this.categoryType = type;
        Pair<HashMap<String,Integer>,Integer> temp = idf();
        this.hm = temp.getKey();
        this.size = temp.getValue();
    }

    private int getSize(){
        return size;
    }
    private int getFrequencyOfWord(String word){
        if(hm.containsKey(word)){
            return hm.get(word);
        }
        else{
            return 1;
        }
    }


    public synchronized HashMap<String,Double> tfIdf(Article a){
        HashMap<String,Double> tf= new HashMap<>();
        String[] words = a.getContent().split(" ");
        for(String word : words){
            if(tf.containsKey(word)){
                tf.put(word,tf.get(word)+1);
            }
            else{
                tf.put(word,1.0);
            }
        }
        int size = getSize();
        Iterator hmIterator = tf.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            double normaliseValue = ((double)mapElement.getValue()/words.length);
            int frequency = getFrequencyOfWord((String)mapElement.getKey());
            double idf = Math.log(size/frequency);
            tf.put((String)mapElement.getKey(),normaliseValue*idf);
        }
        return tf;
    }


    /*
        Return: returns a hashmap that contains word appearance in all articles and total number of articles
    */
    private Pair<HashMap<String,Integer>,Integer> idf(){
        HashMap<String,Integer> ret = new HashMap<>();
        List<Article> articles = DBConnect.getInstance().fetchArticles(categoryType);
        for(Article article:articles){
            String content = article.getContent();
            String[] words = content.split(" ");
            HashMap<String,Boolean> temp = new HashMap<>();
            for(String word : words){
                if(!temp.containsKey(word)){
                    temp.put(word,true);
                }
            }
            Iterator hmIterator = temp.entrySet().iterator();
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                if(ret.containsKey(mapElement.getKey())){
                    ret.put((String)mapElement.getKey(),ret.get(mapElement.getKey())+1);
                }
                else{
                    ret.put((String)mapElement.getKey(),1);
                }
            }
        }
        return new Pair(ret,articles.size());
    }
}

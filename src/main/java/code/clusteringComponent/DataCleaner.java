package code.clusteringComponent;

import code.contentComponent.RssController;
import code.utility.Log;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataCleaner {
    static List<String> stopwords;

    static public String clean(String s){
        s = s.toLowerCase();
        s = s.trim().replaceAll("[\\p{Punct}]", " ");
        s = s.replaceAll("[^\\x00-\\x7F]", "");
        s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");
        s = s.replaceAll("\\p{C}", " ");
        s = s.replaceAll("[^a-zA-Z0-9]", " ");
        s = s.replaceAll(" +", " ");
        s = removeStopWords(s);
        Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        s = Arrays.stream(s.split(" ")).map(word -> {
            return stemmer.stem(word);
        }).collect(Collectors.joining(" "));
        return s;
    }

    public static void loadStopwords(){
        if(stopwords != null){
            return;
        }
        try(InputStream s = RssController.class.getResourceAsStream("/english_stopwords.txt")) {
            stopwords = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(s));
            String line;
            while((line = reader.readLine()) != null) {
                stopwords.add(line);
            }
//            System.out.println(stopwords);
        } catch (Exception e) {
            Log.debug(e.getMessage());
        }
    }

    public static String removeStopWords(String text){
        loadStopwords();
        ArrayList<String> allWords =
                Stream.of(text.split(" "))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);

        String result = allWords.stream().collect(Collectors.joining(" "));
        return result;
    }
}

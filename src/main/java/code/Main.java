package code;

import code.clusteringComponent.Cluster;
import code.clusteringComponent.DBScanClusterer;
import code.clusteringComponent.DataCleaner;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.utility.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void delete(File file)
            throws IOException{

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
    public static String stringify(Article a){
        try {
            return a.getTitle() + " _______ " + a.getUrl() + " \n";
        }
        catch (Exception e){
            Log.error(e.getMessage());
        }
        return "null";
    }
    public static void main(String[] args) throws IOException {

//        System.out.println(DataCleaner.clean("Hello ' th'e of i demo's sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));

        //        System.out.println(DataCleaner.clean("Hello ' th'e of i sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
//        PollingService.getInstance().poll();

        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
        DBScanClusterer<Article> clusterer = new DBScanClusterer<>(0.6, 2);
        int count = 0;
        List<Cluster<Article>> clusters = clusterer.cluster(articles);
        File directory = new File("clusters");
        if(!directory.exists()){
            System.out.println("Directory does not exist.");
        }else{
            try{
                delete(directory);

            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        File d = new File("clusters");
        d.mkdir();
        System.out.println("Total Clusters: "+clusters.size());
        for(Cluster<Article> cluster: clusters){
            File file = new File("clusters/"+count);
            count++;
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            FileWriter writer = new FileWriter(file);
//            writer.write("Center:  "+ stringify(cluster.getCenter())+" \n\n");
            for(Article a : cluster.getPoints()){
                writer.write(stringify(a));
            }
            writer.close();
        }

        /* Test for tf-idf function
        List<Article> list = new ArrayList<>();
        Article a = new ArticleBuilder("url4")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This is random shit")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        Article b = new ArticleBuilder("url5")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This 'is' Bull' shit")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        Article c = new ArticleBuilder("url3")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("This is a great\" article")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();
        list.add(a);
        list.add(b);
        list.add(c);

         */

    }
}

package code;
import code.clusteringComponent.DBScanClusterer;

import code.clusteringComponent.BatchClusterService;

import code.clusteringComponent.IncrementalClusterService;
import code.contentComponent.PollingService;
import code.databaseService.DBConnect;
import code.models.Article;
import code.models.CategoryType;
import code.models.Cluster;
import code.utility.GlobalFunctions;

public class Main {
    public static void main(String[] args)  {

        //        System.out.println(DataCleaner.clean("Hello ' th'e of i sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
//        PollingService.getInstance().poll();

//        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        DBScanClusterer<Article> clusterer = new DBScanClusterer<>(0.5, 4);
        BatchClusterService.getInstance().start();
        IncrementalClusterService.getInstance().start();
        PollingService.getInstance().poll();

//        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        DBScanClusterer<Article> clusterer = new DBScanClusterer<>(0.6, 2);
////        int count = 0;

//        List<Cluster<Article>> clusters = clusterer.cluster(articles);
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        for(Cluster c: clusters){
//            for(Object a: c.getPoints()){
//                Article article = (Article) a;
//                hashMap.put(article.getId(), c.getClusterId());
//            }
//        }
//        DBConnect.getInstance().updateClusterIDs(hashMap);
////        GlobalFunctions.dumpClusters(clusters);


//        Article article = new ArticleBuilder("https://www.tribuneindia.com/news/world/pell-loses-appeal-against-sex-abuse-convictions-returns-to-prison/820587.html ")
//                .setCategoryType(CategoryType.WORLD).setPublishedDate(new Date()).setRssLink("url").setTitle("An Australian Court Has Upheld Child Sex Convictions Against Cardinal George Pell")
//                .setContent(GlobalFunctions.extractFromUrl("https://time.com/5657201/cardinal-george-pell-child-sex-abuse-conviction-australia/")).build();
//        ArrayList<Article> a = new ArrayList<>();
//        a.add(article);
//        HashMap<String, Integer> hashMap1 = clusterer.clusterIncrementally(a);
//        System.out.println(hashMap1);

        /* Test for tf-idf function
        List<Article> list = new ArrayList<>();

        Article a = new ArticleBuilder("url4")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("i am vipin kumar u")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();

        Article b = new ArticleBuilder("url3")
                .setCategoryType(CategoryType.SPORTS)
                .setContent("i am vipin kumar")
                .setRssLink("Rss")
                .setTitle("demo")
                .setPublishedDate(new Date())
                .build();

        List<Article> list = new ArrayList<>();
        list.add(a);
        list.add(b);

        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship();
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
            Article c = (Article) mapElement.getKey();
            System.out.println(c.getUrl() + " " + mapElement.getValue());

        }
        // Test for tf-idf function
//        List<Article> list = new ArrayList<>();
//
//        Article a = new ArticleBuilder("url4")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("i am vipin kumar u")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        Article b = new ArticleBuilder("url3")
//                .setCategoryType(CategoryType.SPORTS)
//                .setContent("i am vipin kumar")
//                .setRssLink("Rss")
//                .setTitle("demo")
//                .setPublishedDate(new Date())
//                .build();
//
//        List<Article> list = new ArrayList<>();
//        list.add(a);
//        list.add(b);
//
//        HashMap<Article,Integer> hashMap = DBConnect.getInstance().articleClusterRelationship();
//        Iterator iterator = hashMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry mapElement = (Map.Entry)iterator.next();
//            Article c = (Article) mapElement.getKey();
//            System.out.println(c.getUrl() + " " + mapElement.getValue());
//
//        }


//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

//        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        System.out.println(list.size());
//        HierarchicalClusterer<Article> hierarchicalCluster = new HierarchicalClusterer<Article>(0.45);
//        hierarchicalCluster.cluster(list);
//        List<Cluster<Article>> clusters = hierarchicalCluster.cluster(list);
//        try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            Log.error("Unable to dump clusters");
//        }


//        HashMap<String,Integer> hashMap = new HashMap<>();
//        hashMap.put("0044625c8241a4728d35bd2b7402fb60",1);
//        hashMap.put("0049220afeb67fd2e2326b203450649b",5);
//        DBConnect.getInstance().updateClusterIDs(hashMap);

        System.out.println(DBConnect.getInstance().maxClusterId());


        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("0044625c8241a4728d35bd2b7402fb60",1);
        hashMap.put("0049220afeb67fd2e2326b203450649b",5);
        DBConnect.getInstance().updateClusterIDs(hashMap);

         */
    }
}
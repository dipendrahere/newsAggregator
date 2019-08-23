package code;

import code.contentComponent.PollingService;



public class Main {
    public static void main(String[] args) {


//        System.out.println(DataCleaner.clean("Hello ' th'e of i demo's sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));

        //        System.out.println(DataCleaner.clean("Hello ' th'e of i sas sfdfe ! %$ @, ,,  paying playing player played saying sayer says paid done doer goer goes }} {{ , 303  /["));
//        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        PollingService.getInstance().poll();

//        List<Article> articles = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        DBScanClusterer<Article> clusterer = new DBScanClusterer<>(0.6, 2);
//        int count = 0;
//        List<Cluster<Article>> clusters = clusterer.cluster(articles);


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


//        try {
//            System.out.println(GlobalFunctions.cosineSimilarity(b,a));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

//        List<Article> list = DBConnect.getInstance().fetchArticles(CategoryType.WORLD);
//        System.out.println(list.size());
//        HierarchicalClusterer<Article> hierarchicalCluster = new HierarchicalClusterer<Article>(0.7);
//        List<Cluster<Article>> clusters = hierarchicalCluster.cluster(list);
//        try {
//            GlobalFunctions.dumpClusters(clusters);
//        } catch (IOException e) {
//            Log.error("Unable to dump clusters");
//        }
*/
    }
}
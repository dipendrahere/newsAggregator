package code.databaseService;

import code.models.*;
import code.utility.Log;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

import java.util.*;

import java.sql.*;
import java.util.stream.Collectors;

public class DBConnect {
    private static Connection connnection;
    private static Statement statment;
    private static ResultSet resultSet;
    private static DBConnect db;

    public static synchronized DBConnect getInstance(){
        if (db==null)
            db = new DBConnect();
        return db;
    }

    private DBConnect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connnection = DriverManager.getConnection("jdbc:mysql://172.19.33.103:3306/newsaggregator","root","kEMXdVW9vMvQ");
//            connnection = DriverManager.getConnection("jdbc:mysql://localhost/newsaggregator","root","vipin1407");
            statment = connnection.createStatement();
        }
        catch (ClassNotFoundException e) {
            Log.error("JDBC not available");
        } catch (SQLException e) {
            Log.error(e.getMessage());
            e.printStackTrace();
            Log.error("Unable to make connection to DB");
        }
    }

    public static synchronized void insertArticles(List<Article> articles){
        Log.debug("INSERTING ARTICLES");
        if(articles.size() == 0){
            return;
        }
        try{
            articles = articles.stream().filter(article -> article.getPublishedDate() != null).collect(Collectors.toList());
            articles = articles.stream().filter(article -> {
                return article.getContent().split(" ").length >= 30 && article.getContent().length() <= 64000;
            }).collect(Collectors.toList());
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = "(id, title, category_id, url, publishedDate, rssLink, content, imageUrl)";
            PreparedStatement preparedStatement = connnection.prepareStatement("insert into articles "+ format +" values(?, ?, ?, ?, ?, ?, ?, ?);");
            PreparedStatement clusterPreparedStatement = connnection.prepareStatement("insert into clusterArticleRelationship(articleId, categoryId) values (?,?);");
            for(int i=0;i<articles.size();i++) {
                Article article = articles.get(i);
                String exactDate = null;
                if(article.getPublishedDate() != null){
                    exactDate = simpleDateFormat.format(article.getPublishedDate());
                }
                preparedStatement.setString(1, article.getId());
                preparedStatement.setString(2, article.getTitle());
                preparedStatement.setInt(3, article.getCategoryType().value.getKey());
                preparedStatement.setString(4, article.getUrl());
                preparedStatement.setString(5, exactDate);
                preparedStatement.setString(6, article.getRssLink());
                preparedStatement.setString(7, article.getContent());
                preparedStatement.setString(8, article.getImageUrl());
                preparedStatement.addBatch();

                clusterPreparedStatement.setString(1, article.getId());
                clusterPreparedStatement.setInt(2, article.getCategoryType().value.getKey());
                clusterPreparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            clusterPreparedStatement.executeBatch();

            Log.debug("ARTICLES INSERTION PROCESSED: "+articles.size());

        }
        catch (SQLException e){
            Log.error(e.getMessage());
            e.printStackTrace();
            Log.error("Unable to insert bulk Articles");
        }
    }

    public void insertArticle(Article article){
        try{
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String exactDate = simpleDateFormat.format(article.getPublishedDate());
            PreparedStatement preparedStatement = connnection.prepareStatement("insert into articles values (?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, article.getId());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setInt(3, article.getCategoryType().value.getKey());
            preparedStatement.setString(4, article.getUrl());
            preparedStatement.setString(5, exactDate);
            preparedStatement.setString(6, article.getRssLink());
            preparedStatement.setString(7, article.getContent());
            preparedStatement.setString(8, article.getImageUrl());
            int count = preparedStatement.executeUpdate();
            System.out.println("insert row "+count);
        }
        catch (SQLException e){
            Log.error("Unable to insert Article: "+ article);
        }
    }

    public static synchronized boolean isArticlePresent(String url){
        boolean ret = true;
        try {
            String query = "select * from articles where url = \""+url+"\";";
            resultSet = statment.executeQuery(query);
            if (!resultSet.isBeforeFirst() ) {
                ret = false;
            }
        } catch (SQLException e) {
            Log.error("Unable to query for article present url: " + url + e.getMessage());
        }
        return ret;
    }

    // Todo Donot remove this function
    public static synchronized List<Article> fetchArticles(CategoryType categoryType){
        List<Article> ret = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connnection.prepareStatement("select * from articles where category_id = "+categoryType.value.getKey()+";");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article a = new ArticleBuilder(resultSet.getString(4))
                        .setTitle(resultSet.getString(2))
                        .setCategoryType(CategoryType.values()[resultSet.getInt(3)-1])
                        .setPublishedDate(resultSet.getDate(5))
                        .setRssLink(resultSet.getString(6))
                        .setContent(resultSet.getString(7))
                        .setImageUrl(resultSet.getString(8))
                        .build();
                a.setId(resultSet.getString(1));
                ret.add(a);
            }
        }
        catch (SQLException e){
            Log.error("unable to fetch Article " +e.getMessage());
        }
        return ret;
    }

    public static synchronized List<Article> fetchArticlesRecent(CategoryType categoryType){
        List<Article> ret = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connnection.prepareStatement("select * from articles where category_id = "+categoryType.value.getKey() + " order by publishedDate desc limit 2000");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article a = new ArticleBuilder(resultSet.getString(4))
                        .setTitle(resultSet.getString(2))
                        .setCategoryType(CategoryType.values()[resultSet.getInt(3)-1])
                        .setPublishedDate(resultSet.getDate(5))
                        .setRssLink(resultSet.getString(6))
                        .setContent(resultSet.getString(7))
                        .setImageUrl(resultSet.getString(8))
                        .build();
                a.setId(resultSet.getString(1));
                ret.add(a);
            }
        }
        catch (SQLException e){
            Log.error("unable to fetch Article " +e.getMessage());
        }
        return ret;
    }


    public static synchronized void updateClusterIDs(HashMap<String,Integer> hashMap){
        Log.debug("Update in Db");
//        System.out.println(hashMap);
        if(hashMap.size() == 0){
            return;
        }
        try{
            PreparedStatement preparedStatement = connnection.prepareStatement("Update clusterArticleRelationship set cluster_id = ? where articleId = ?");
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry mapElement = (Map.Entry)iterator.next();
                preparedStatement.setInt(1,(Integer)mapElement.getValue());
                preparedStatement.setString(2,(String)mapElement.getKey());
                preparedStatement.addBatch();
            }
            preparedStatement.executeLargeBatch();
        }
        catch (SQLException e){
            Log.error(e.getMessage());
            e.printStackTrace();
            Log.error("Unable to update Cluster id of Articles");
        }

    }

    public static synchronized HashMap<Article, Integer> articleClusterRelationship(CategoryType categoryType){
        HashMap<Article,Integer> ret = new HashMap<>();
        try{
            PreparedStatement preparedStatement = connnection.prepareStatement("select * from articles join clusterArticleRelationship on articles.id = clusterArticleRelationship.articleId where articles.category_id = ?");
            preparedStatement.setInt(1,categoryType.value.getKey());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article a = new ArticleBuilder(resultSet.getString(4))
                        .setTitle(resultSet.getString(2))
                        .setCategoryType(CategoryType.values()[resultSet.getInt(3)-1])
                        .setPublishedDate(resultSet.getDate(5))
                        .setRssLink(resultSet.getString(6))
                        .setContent(resultSet.getString(7))
                        .setImageUrl(resultSet.getString(8))
                        .build();
                a.setId(resultSet.getString(1));
                int cluster_id = resultSet.getInt(10);
                ret.put(a,cluster_id);
            }
        }
        catch (SQLException e){
            Log.error("unable to fetch Article "+ e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }

    public static synchronized int maxClusterId(){
        int ret = 1;
        try {
            PreparedStatement preparedStatement = connnection.prepareStatement(" select max(cluster_id) from clusterArticleRelationship;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ret = resultSet.getInt(1);
            }
        }
        catch (SQLException e){
            Log.error("unable to find max clusterId "+ e.getMessage());
        }
        return ret;

    }

    public static synchronized void unassignClusters(CategoryType categoryType){
        try{
            PreparedStatement preparedStatement = connnection.prepareStatement("update clusterArticleRelationship set cluster_id = NULL where categoryId = "+categoryType.value.getKey());
            preparedStatement.executeLargeUpdate();
        }catch (SQLException e){
            Log.error("unable to unset clustering " + e.getMessage());
        }
    }

    public static synchronized List<ClusterInfo> getClusterInfo(){
        List<ClusterInfo> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connnection.prepareStatement("select * from clusterInfo");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ClusterInfo clusterInfo = new ClusterInfo();
                clusterInfo.setClusterId(resultSet.getInt(1));
                clusterInfo.setRecency(resultSet.getDate(3));
                clusterInfo.setTotalPoints(resultSet.getInt(4));
                clusterInfo.setAverageDate(resultSet.getDate(5));
                clusterInfo.setDiameter(resultSet.getDouble(6));
                clusterInfo.addRssLinks(Arrays.asList(resultSet.getString(2).split("\\|")));
                list.add(clusterInfo);
            }
        }
        catch (Exception e){
            Log.error("unable to fetch clusterInfo " + e.getMessage());
        }
        return list;
    }

    public static synchronized void updateClusterInfo(List<ClusterInfo> list) throws NullArgumentException {
        MathUtils.checkNotNull(list);
        try {
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String exactDate = null;
            PreparedStatement preparedStatement = connnection.prepareStatement("INSERT INTO clusterInfo VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE recency=?,totalPoints=?,averageDate=?,diameter=?,rssLinks=?");
            for(ClusterInfo clusterInfo : list){
                preparedStatement.setInt(1,clusterInfo.getClusterId());
                if(clusterInfo.getRecency() != null){
                    exactDate = simpleDateFormat.format(clusterInfo.getRecency());
                }
                preparedStatement.setString(3,exactDate);
                preparedStatement.setString(7,exactDate);
                exactDate = null;
                preparedStatement.setInt(4,clusterInfo.getTotalPoints());
                preparedStatement.setInt(8,clusterInfo.getTotalPoints());
                if(clusterInfo.getAverageDate() != null){
                    exactDate = simpleDateFormat.format(clusterInfo.getAverageDate());
                }
                preparedStatement.setString(5,exactDate);
                preparedStatement.setString(9,exactDate);

                preparedStatement.setDouble(6,clusterInfo.getDiameter());
                preparedStatement.setDouble(10,clusterInfo.getDiameter());
                String rssLinks = "";
                for(String rssLink : clusterInfo.getDistinctRss()){
                    rssLink= rssLink.trim();
                    rssLinks += rssLink;
                    rssLinks += "|";
                }
                preparedStatement.setString(2,rssLinks);
                preparedStatement.setString(11,rssLinks);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        catch (Exception e){
            Log.error("unable to update cluster info "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized void updateClusterRank(HashMap<String,Double> hashMap){
        try {
            PreparedStatement preparedStatement = connnection.prepareStatement("update clusterArticleRelationship set articleRank = ? where articleId = ?");
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Integer> mapElement = (Map.Entry)iterator.next();
                preparedStatement.setString(2, mapElement.getKey());
                preparedStatement.setDouble(1, mapElement.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
        catch (Exception e){
            Log.error("unable to update cluster rank" + e.getMessage());
        }
    }
}

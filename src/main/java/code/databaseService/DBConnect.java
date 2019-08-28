package code.databaseService;

import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import code.utility.Log;
import java.util.*;

import java.sql.*;

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
            connnection = DriverManager.getConnection("jdbc:mysql://localhost/newsaggregator","root","vipin1407");
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
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = "(id, title, category_id, url, publishedDate, rssLink, content, imageUrl)";
            PreparedStatement preparedStatement = connnection.prepareStatement("insert into articles "+ format +" values(?, ?, ?, ?, ?, ?, ?, ?);");
            PreparedStatement clusterPreparedStatement = connnection.prepareStatement("insert into clusterArticleRelationship values (?,?);");
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
                clusterPreparedStatement.setString(2,null);
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
            PreparedStatement preparedStatement = connnection.prepareStatement("select * from articles where category_id = "+categoryType.value.getKey());
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
        System.out.println(hashMap);
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
}

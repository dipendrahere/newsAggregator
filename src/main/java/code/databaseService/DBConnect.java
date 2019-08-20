package code.databaseService;

import code.models.Article;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.sql.*;
import java.util.List;

public class DBConnect {
    private static Connection connnection;
    private static Statement statment;
    private static ResultSet resultSet;
    private static DBConnect db;

    public static DBConnect getInstance(){
        if (db==null)
            db = new DBConnect();
        return db;
    }

    private DBConnect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newsaggregator     ","root","vipin1407");
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
        if(articles.size() == 0){
            return;
        }
        try{
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PreparedStatement preparedStatement = connnection.prepareStatement("insert into articles values (?,?,?,?,?,?,?);");
            for(int i=0;i<articles.size();i++) {
                Article article = articles.get(i);
                String exactDate = simpleDateFormat.format(article.getPublishedDate());
                preparedStatement.setString(1, article.getId());
                preparedStatement.setString(2, article.getTitle());
                preparedStatement.setInt(3, article.getCategoryType().value);
                preparedStatement.setString(4, article.getUrl());
                preparedStatement.setString(5, exactDate);
                preparedStatement.setString(6, article.getRssLink());
                preparedStatement.setString(7, article.getContent());
                preparedStatement.addBatch();
            }
            int result[] = preparedStatement.executeBatch();
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
            PreparedStatement preparedStatement = connnection.prepareStatement("insert into articles values (?,?,?,?,?,?,?);");
            preparedStatement.setString(1, article.getId());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setInt(3, article.getCategoryType().value);
            preparedStatement.setString(4, article.getUrl());
            preparedStatement.setString(5, exactDate);
            preparedStatement.setString(6, article.getRssLink());
            preparedStatement.setString(7, article.getContent());
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
            String id = GlobalFunctions.getMd5(url);
            String query = "select * from articles where id = \""+id+"\";";
            resultSet = statment.executeQuery(query);
            if (!resultSet.isBeforeFirst() ) {
                ret = false;
            }
        } catch (SQLException e) {
            Log.error("Unable to query for article present url: " + url);
        }
        return ret;
    }
}

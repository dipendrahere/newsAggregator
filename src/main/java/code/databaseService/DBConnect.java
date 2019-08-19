package code.databaseService;

import code.models.Article;
import code.utility.GlobalFunctions;
import code.utility.Log;

import java.sql.*;
import java.util.List;

public class DBConnect {
    private Connection connnection;
    private Statement statment;
    private ResultSet resultSet;

    public DBConnect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newsAggregatorDatabase","root","vipin1407");
            statment = connnection.createStatement();

        }
        catch (ClassNotFoundException e) {
            Log.error("JDBC not available");
        } catch (SQLException e) {
            Log.error("Unable to make connection to DB");
        }
    }

    public void insertArticles(List<Article> articles){
        try{
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String query = "insert into articles values";
            for(int i=0;i<articles.size();i++){
                Article article = articles.get(i);
                String exactDate = simpleDateFormat.format(article.getPublishedDate());
                query += "(\"" +article.getId() +"\",\""+article.getId()+"\","+ article.getCategoryType().ordinal()+",\""+
                        article.getUrl()+"\",\""+exactDate+"\",\""+article.getRssLink()+"\",\""+article.getContent()+"\"),";
            }
            if(query.endsWith(",")){
                query = query.substring(0,query.length()-1) + ";";
            }
            int count = statment.executeUpdate(query);
            System.out.println("insert row "+count);
        }
        catch (SQLException e){
            Log.error("Unable to insert bulk Articles");
        }
    }

    public void insertArticle(Article article){
        try{
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String exactDate = simpleDateFormat.format(article.getPublishedDate());
            String query = "insert into articles values(\"" +article.getId() +"\",\""+article.getId()+"\","+article.getCategoryType().ordinal()+",\""+
                    article.getUrl()+"\",\""+exactDate+"\",\""+article.getRssLink()+"\",\""+article.getContent()+"\");";
            System.out.println(query);
            int count = statment.executeUpdate(query);
            System.out.println("insert row "+count);
        }
        catch (SQLException e){
            Log.error("Unable to insert Article: "+ article);
        }
    }

    public boolean isArticlePresent(String url){
        boolean ret = true;
        try {
            String id = GlobalFunctions.getMd5(url);
            String query = "select * from articles where id = \""+id+"\";";
            System.out.println(query);
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

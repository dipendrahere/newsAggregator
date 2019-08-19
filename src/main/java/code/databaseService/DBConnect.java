package code.databaseService;

import shared.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertArticle(Article article){
        try{
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String exactDate = simpleDateFormat.format(article.getPubDate());
            String query = "insert into articles values(" +article.getId() +",\""+article.getTitle()+"\","+article.getCategoryId()+",\""+
                    article.getUrl()+"\",\""+exactDate+"\",\""+article.getRss()+"\",\""+article.getContent()+"\");";
            int count = statment.executeUpdate(query);
            System.out.println("insert row "+count);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCategoryId(String category){
        int ret = 0;
        try {
            String query = "select id from category where name = \""+category+"\";";
            resultSet = statment.executeQuery(query);
            while (resultSet.next()){
                ret = Integer.parseInt(resultSet.getString("id"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}

package shared;

import java.util.Date;

public class Article {
    private int id;
    private String title;
    private int categoryId;
    private String url;
    private Date pubDate;
    private String content;
    private String rss;

    public Article(int id,String title, int categoryId,String url,Date pubDate,String content,String rss){
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.url = url;
        this.pubDate = pubDate;
        this.content = content;
        this.rss = rss;
    }

    public int getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public int getCategoryId(){
        return this.categoryId;
    }
    public String getUrl(){
        return this.url;
    }
    public Date getPubDate(){
        return this.pubDate;
    }
    public String getContent(){
        return this.content;
    }
    public String getRss(){
        return this.rss;
    }
}

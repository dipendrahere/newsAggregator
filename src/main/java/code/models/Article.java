package code.models;

import code.utility.GlobalFunctions;
import code.utility.Log;

import java.util.Date;

public class Article {
    private String id;
    private CategoryType categoryType;
    private Date publishedDate;
    private String url;
    private String content;
    private String title;
    private String rssLink;

    public Article(String url){
        this.url = url;
        this.id = GlobalFunctions.getMd5(url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public int hashCode() {
        return content.hashCode();
    }

    public String toString() {
        try {
            return getTitle() + " _______ " + getUrl() + " \n";
        }
        catch (Exception e){
            Log.error(e.getMessage());
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Article other = (Article) obj;
        if (hashCode() != other.hashCode())
            return false;
        if(this.getId() != other.getId()){
            return false;
        }
        return true;
    }

}



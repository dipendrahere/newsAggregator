package code.models;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.Date;

public class RSSItem {
    private SyndEntry item;
    private String rssLink;

    public RSSItem(SyndEntry item, String rssLink){
        this.item = item;
        this.rssLink = rssLink;
    }

    public String getRssLink() {
        return rssLink;
    }

    public String getTitle(){
        return item.getTitle();
    }

    public Date getPubDate(){
        return item.getPublishedDate();
    }

    public String getDescription(){
        return item.getDescription().getValue();
    }

    public String getLink(){
        return item.getLink();
    }
}

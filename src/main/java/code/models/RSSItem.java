package code.models;
import code.utility.Log;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import java.util.List;

import java.util.Date;

public class RSSItem {
    private SyndEntry item;
    private String rssLink;

    public RSSItem(SyndEntry item, String rssLink){
        this.item = item;
        this.rssLink = rssLink;
    }

    public String getImageUrl(){
        String ret = "";
        List<SyndEnclosure> encls = item.getEnclosures();
        if(!encls.isEmpty()){
            for(SyndEnclosure e : encls){
                String imgURL = e.getUrl().toString();
                ret += imgURL;
                break;
            }
            return ret;
        }
        else{
            return null;
        }
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

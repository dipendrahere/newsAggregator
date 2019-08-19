package code.contentComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import code.models.Article;
import code.models.ArticleBuilder;
import code.models.CategoryType;
import code.models.RSSItem;
import code.utility.GlobalFunctions;
import code.utility.Log;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;


public class RssController {

    public void visitCategory(String filePath){
        List<String> rssList = getRSSLinksfromFile(filePath);
        List<CompletableFuture<List<Article>>> allRssCompletable = new ArrayList<>();
        for(String rssLink: rssList){
            CompletableFuture<List<Article>> futureForRss = getArticlesFromRss(rssLink);
            allRssCompletable.add(futureForRss);
        }
        CompletableFuture<List<Article>>[] arr = allRssCompletable.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .toArray(new CompletableFuture[allRssCompletable.size()]);
        CompletableFuture<Void> allOfRss = CompletableFuture.allOf(arr);
        CompletableFuture<List<Article>> articlefuture = allOfRss.thenApply(f -> {
            return Arrays.stream(arr)
                    .map(future -> future.join())
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        });
        fetchArticlesFromRss(articlefuture);
    }


    private void fetchArticlesFromRss(CompletableFuture<List<Article>> futureForRss) {
        try {
            List<Article> articles = futureForRss.get();
            for (Article a : articles) {
                System.out.println(a.getContent());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<List<Article>> getArticlesFromRss(String rssLink){
        return getEntriesFromRss(rssLink).thenCompose(list -> {
            list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
            CompletableFuture<Article>[] completableFutures = list.stream().map(item -> {
                return getArticlesFromFeed(item);
            }).filter(Objects::nonNull)
                    .collect(Collectors.toList())
                    .toArray(new CompletableFuture[list.size()]);

            CompletableFuture<Void> future = CompletableFuture.allOf(completableFutures);
            CompletableFuture<List<Article>> articlefuture = future.thenApply(f -> {
                return Arrays.stream(completableFutures)
                        .map(completableFuture -> completableFuture.join())
                        .collect(Collectors.toList());
            });
            return articlefuture;
        }).exceptionally(ex -> {
            Log.error("Problem occurred when " + rssLink + " was parsed "+ex.getMessage());

            return null;
        });
    }


    private List<String> getRSSLinksfromFile(String filePath){
        ArrayList<String> rssList = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                rssList.add(line);
            }
        } catch (Exception e) {
            Log.debug(e.getMessage());
        }
        return rssList;
    }

    private Boolean isLinkRead(String link){
        return true;
    }

    private CompletableFuture<List<RSSItem>> getEntriesFromRss(String path){
        return CompletableFuture.supplyAsync(() -> {
            List<RSSItem> rssItems = new ArrayList<>();
            try {
                rssItems = rssItemsFromXML(path);
            } catch (MalformedURLException e) {
                Log.error("URL: " + path + " is malformed");
            }
            return rssItems;
        });
    }

    private List<RSSItem> rssItemsFromXML(String path) throws MalformedURLException{

        List<RSSItem> rssItems = new ArrayList<>();
        try (XmlReader reader = new XmlReader(new URL(path))) {
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (SyndEntry entry : feed.getEntries()) {
                RSSItem item = new RSSItem(entry, path);
                rssItems.add(item);
            }
            Log.debug("READ: " +  path);
        } catch (IOException e) {
            Log.error("Could not create XML reader");
        } catch (FeedException e) {
            Log.error("Unable to read feed");
        }
        return rssItems;

    }

    private void writeInDB(Article article){
        System.out.println(article.getTitle());
    }

    private CompletableFuture<Article> getArticlesFromFeed(RSSItem item){
        return CompletableFuture.supplyAsync(() -> {
            ArticleBuilder articleBuilder = new ArticleBuilder(item.getLink());
            try {
                articleBuilder.setCategoryType(CategoryType.SPORTS)
                        .setPublishedDate(item.getPubDate())
                        .setRssLink(item.getRssLink())
                        .setContent(GlobalFunctions.extractFromUrl(item.getLink()))
                        .setTitle(item.getTitle());
            } catch (BoilerpipeProcessingException e) {
                Log.error("Unable to extract data from url - boilerpipe");
            } catch (MalformedURLException e) {
                Log.error("Unable to form url for " + item.getLink());
            } catch (Exception e) {
                Log.error("Something went wrong");
            }
            Article article = articleBuilder.build();
            return article;
        }).exceptionally(ex -> {
            Log.error("Something happened when " + item.getLink() + " was crawled");
            return null;
        });
    }
}
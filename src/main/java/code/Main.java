package code;

import code.contentComponent.RssController;

public class Main {
    public static void main(String[] args) {
        RssController rssController = new RssController();
        rssController.visitCategory("src/main/resources/rssSports.txt");
    }
}

package Crawler.linkExtactor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;

/**
 * An Class that extracts links using Jsoup.
 */
public class JsoupLinkExtractor implements LinkExtractor {

    /**
     * extract links from content.
     * @param content the content from which to extract links.
     * @return the links extracted
     */
    @Override
    public Iterable<String> extractLinksFromString(String content) {
        Document document = Jsoup.parse(content);
        document.html();
        List<String> links = document.select("a[href]").eachAttr("href");
        return links;
    }
}
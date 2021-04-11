package Crawler.linkExtactor;

/**
 * An interface for classes the extractor links.
 */
public interface LinkExtractor {

    /**
     * extract links from content.
     * @param content the content from which to extract links.
     * @return the links extracted
     */
    Iterable<String> extractLinksFromString(String content);
}

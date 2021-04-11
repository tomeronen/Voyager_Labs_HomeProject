package Crawler.pageDataDownloader;

/**
 * An interface for classes the download data.
 */
public interface PageDataDownloader {


    /**
     * download data.
     * @param content content to be downloaded.
     * @param url the url of the content.
     * @param depth the depth of the url.
     */
    void downloadDataFromString(final String content, String url, int depth);
}

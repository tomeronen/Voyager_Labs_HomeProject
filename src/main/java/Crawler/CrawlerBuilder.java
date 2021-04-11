package Crawler;

import Crawler.linkExtactor.JsoupLinkExtractor;
import Crawler.linkExtactor.LinkExtractor;
import Crawler.pageDataDownloader.PageDataDownloader;
import Crawler.pageDataFetcher.PageDataFetcher;

/**
 * A Builder Of Crawler Tasks.
 * Deciding how you want to build your crawler might be complex. The builder class lets you do it
 * step by step if need to, and easily create multiple incidental Crawlers if needed to.
 */
public class CrawlerBuilder {
    private LinkExtractor linkExtractor;
    private PageDataDownloader pageDataCollector;
    private PageDataFetcher pageDataFetcher;

    /**
     * Sets the Link Extractor to be used in crawlers.
     * @param linkExtractor the Link Extractor to be set.
     * @return The current CrawlerBuilder for chaining
     */
    public CrawlerBuilder setLinkExtractor(JsoupLinkExtractor linkExtractor){
        this.linkExtractor = linkExtractor;
        return this;
    }

    /**
     * Sets the PageDataDownloader to be used in crawlers.
     * @param pageDataCollector the PageDataDownloader to be set.
     * @return The current CrawlerBuilder for chaining
     */
    public CrawlerBuilder setPageDataCollector(PageDataDownloader pageDataCollector) {
        this.pageDataCollector = pageDataCollector;
        return this;
    }

    /**
     * Sets the PageDataFetcher to be used in crawlers.
     * @param pageDataFetcher the PageDataFetcher to be set.
     * @return The current CrawlerBuilder for chaining
     */
    public CrawlerBuilder setPageDataFetcher(PageDataFetcher pageDataFetcher) {
        this.pageDataFetcher = pageDataFetcher;
        return this;
    }


    /**
     * Builds a crawler using the data given.
     * @return the crawler that was built.
     */
    public Crawler build() {
        return new Crawler(this.linkExtractor, this.pageDataCollector, this.pageDataFetcher);
    }
}

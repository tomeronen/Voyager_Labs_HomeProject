package Crawler;

import java.util.Iterator;


/**
 * Represents a signal Crawler Task:
 *  1) getting data from url
 *  2) save data.
 *  3) if needed extract more links and start tasks on new links.
 */
public class CrawlerTask implements Runnable {

    private int maxCrawlAttempts = 3;

    private final String curUrl;
    private final Crawler crawler;
    private int depth;

    /**
     * Constructor for a crawler task.
     * @param crawler the crawler this task belongs to.
     * @param curUrl the url to crawl to.
     * @param crawlDepth the current depth of task.
     */
    CrawlerTask(Crawler crawler, String curUrl, int crawlDepth) {
        this.crawler = crawler;
        this.curUrl = curUrl;
        this.depth = crawlDepth;
    }

    /**
     * Preforms the crawling task.
     */
    @Override
    public void run() {
        if(HttpLink.isCrawlable(this.curUrl)) {
            CrawlerTask crawlerWorker = this;
            new MultiAttemptProcess(
                () -> {
                    this.crawler.workCount.incrementAndGet();
                    String urlData = crawlerWorker.crawler.getPageDataFetcher().fetchData(crawlerWorker.curUrl);
                    crawlerWorker.crawler.getPageDataDownloader().downloadDataFromString(urlData, curUrl, depth);
                    if (crawlerWorker.depth < crawlerWorker.crawler.getCrawlDepth()) {
                        crawlerWorker.crawlToNextPages(urlData);
                    }
                    this.crawler.workCount.decrementAndGet();
                },
                () -> this.crawler.workCount.decrementAndGet(),
                    maxCrawlAttempts).start();
        }
    }

    /**
     * Extract links from current location start new crawling tasks on those links.
     * @param content the data of the current location.
     */
    private void crawlToNextPages(String content){
            int counter = 0;
            final Iterator<String> links = this.crawler.getLinkExtractor().extractLinksFromString(content).iterator();
            while(links.hasNext() && counter < this.crawler.getMaxAmountOfUrls()){
                String nextLink = links.next();
                if(HttpLink.isCrawlable(nextLink) && this.crawler.checkAndUpdateSeenUrls(nextLink, this.depth + 1)){
                    // System.out.println(depth +":" + curUrl + ":" + counter + ":" + nextLink);
                    this.crawler.getThreadPool().execute(
                        new CrawlerTask(this.crawler, nextLink,this.depth + 1));
                    ++counter;
                }
            }
    }



}

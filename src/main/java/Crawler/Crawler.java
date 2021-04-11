package Crawler;

import Crawler.linkExtactor.LinkExtractor;
import Crawler.pageDataDownloader.PageDataDownloader;
import Crawler.pageDataFetcher.PageDataFetcher;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Crawler{

    private ThreadPoolExecutor threadPool;
    private PageDataFetcher pageDataFetcher;
    private PageDataDownloader pageDataDownloader;
    private LinkExtractor linkExtractor;

    private int maxAmountOfUrls;
    private int crawlDepth;
    private boolean crossLevelUniqueness;

    private List<Set<String>> seenUrlsByDepth;

    private ReentrantLock checkAndUpdateMultiDepthLock = new ReentrantLock();
    AtomicInteger workCount = new AtomicInteger(0);

    /**
     * Constructor for Crawler.
     * @param linkExtractor Object implementing LinkExtractor, used to extract links.
     * @param pageDataCollector Object implementing PageDataDownloader, used to save the data.
     */
    public Crawler(LinkExtractor linkExtractor, PageDataDownloader pageDataCollector, PageDataFetcher pageDataFetcher) {
        this.linkExtractor = linkExtractor;
        this.pageDataDownloader = pageDataCollector;
        this.pageDataFetcher = pageDataFetcher;
    }


    /**
     * Start a crawling job.
     * @param startingUrl The url from which to start the crawl.
     * @param maxAmountOfUrls  max amount of urls to take from each location.
     * @param crawlDepth the max depth of the current crawl.
     * @param crossLevelUniqueness True if every url collected should be unique, even cross depth level.
     */
    public void startCrawling(String startingUrl,
                              int maxAmountOfUrls,
                              int crawlDepth,
                              boolean crossLevelUniqueness){

        if(!HttpLink.isCrawlable(startingUrl) || maxAmountOfUrls < 0 || crawlDepth < 0){
            throw new IllegalArgumentException();
        }

        if(this.threadPool != null && this.crawlingRunning()){ // already crawling, close and start new.
            this.closeCrawlerNow();
        }

        this.initialize(maxAmountOfUrls, crawlDepth, crossLevelUniqueness);
        this.checkAndUpdateSeenUrls(startingUrl, 0);
        this.workCount.incrementAndGet();
        threadPool.execute(new CrawlerTask(this, startingUrl, 0));
        this.workCount.decrementAndGet();
    }

    /**
     * initializes the data needed for a crawl.
     * @param maxAmountOfUrls  max amount of urls to take from each location.
     * @param crawlDepth the max depth of the current crawl.
     * @param crossLevelUniqueness True if every url collected should be unique, even cross depth level.
     */
    private void initialize(int maxAmountOfUrls, int crawlDepth, boolean crossLevelUniqueness) {
        this.maxAmountOfUrls = maxAmountOfUrls;
        this.crawlDepth = crawlDepth;
        this.crossLevelUniqueness = crossLevelUniqueness;
        int pollSize = calculateBestPollSize(maxAmountOfUrls, crawlDepth);
        this.threadPool =  (ThreadPoolExecutor) Executors.newFixedThreadPool(pollSize);
        this.seenUrlsByDepth = new ArrayList<>();
        for (int i = 0; i < this.crawlDepth + 1; ++i){
            seenUrlsByDepth.add(Collections.synchronizedSet(new HashSet<>()));
        }
    }

    /**
     * Based on the parameters for this crawling job finds the best Thread Poll Size.
     * @param maxAmountOfUrls max amount of urls to take from each location.
     * @param crawlDepth the max depth of the current crawl.
     * @return The Best Thread Poll size.
     */
    private int calculateBestPollSize(int maxAmountOfUrls, int crawlDepth) {
        // TODO: Need to test how parameters affect needed poll size.
        return 15;
    }


    /**
     * Wait thread until crawler has finished current crawling.
     */
    public void waitCrawling(){
        while(this.crawlingRunning());
    }

    /**
     * @return True if the crawler is currently in the middle of crawling, False otherwise.
     */
    private boolean crawlingRunning() {
        return threadPool.getActiveCount() != 0 ||
                threadPool.getQueue().size() != 0 ||
                workCount.get() != 0 ||
                threadPool.getCompletedTaskCount() == 0;
    }

    /**
     * Closes The Crawler.
     * Shuts down active thread.
     */
    public void closeCrawlerNow(){
        threadPool.shutdownNow();
    }

    /**
     * Closes The Crawler.
     * No new Threads can start, but waits until all active threads finish.
     */
    public void closeCrawler(){
        try {
            System.out.println(seenUrlsByDepth);
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks if the link was already seen.
     * @param curLink the Web link to check.
     * @param depth the depth of the link to check.
     * @return True if the link was not found and added. False otherwise.
     */
    public boolean checkAndUpdateSeenUrls(String curLink, int depth) {
        String urlString = HttpLink.getUrlFileNameStringFormat(curLink);
        if(this.isCrossLevelUniqueness()){
            this.checkAndUpdateMultiDepthLock.lock();
            for(Set<String> linksSeen: this.seenUrlsByDepth){
                if(linksSeen.contains(urlString)){
                    this.checkAndUpdateMultiDepthLock.unlock();
                    return false;
            }
            }
            this.seenUrlsByDepth.get(depth).add(urlString);
            this.checkAndUpdateMultiDepthLock.unlock();
            return true;
        } else{
            return this.seenUrlsByDepth.get(depth).add(urlString);
        }
    }



    // getters:

    /**
     * @return the max depth of this crawler.
     */
    public int getCrawlDepth() {
        return crawlDepth;
    }

    /**
     * @return the LinkExtractor used by this crawler
     */
    public LinkExtractor getLinkExtractor() {
        return linkExtractor;
    }

    /**
     * @return the ThreadPoolExecutor used by this crawler
     */
    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }


    /**
     * @return the PageDataDownloader used by this crawler
     */
    public PageDataDownloader getPageDataDownloader() {
        return pageDataDownloader;
    }

    /**
     * @return The max amount of urls this crawler collects from each page
     */
    public int getMaxAmountOfUrls() {
        return maxAmountOfUrls;
    }

    /**
     * @return True if this crawler every url collected is unique, even cross depth level. False otherwise.
     */
    public boolean isCrossLevelUniqueness() {
        return crossLevelUniqueness;
    }

    public PageDataFetcher getPageDataFetcher() {
        return this.pageDataFetcher;
    }
}

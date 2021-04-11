import Crawler.Crawler;
import Crawler.CrawlerBuilder;
import Crawler.linkExtactor.JsoupLinkExtractor;
import Crawler.pageDataDownloader.SaveToFileDataDownloader;
import Crawler.pageDataFetcher.BufferedPageDataFetcher;

public class Main {

    private static final String WRONG_USAGE_MSG = "Usage Error, use: <Starting Url> <urls per level>" +
                                        " <crawl depth> <cross Level Uniqueness>";

    private static final int NEEDED_ARGS_AMOUNT = 4;


    private static String startingUrl;
    private static int maxAmountOfUrls;
    private static int crawlDepth;
    private static boolean crossLevelUniqueness;


    /**
     * The main function for this program. Shows a test case for our MultiThreaded WebCrawler.
     * @param args
     */
    public static void main(String[] args){
        try{
            extractArgs(args);
            Crawler crawler = new CrawlerBuilder()
                    .setLinkExtractor(new JsoupLinkExtractor())
                    .setPageDataCollector(new SaveToFileDataDownloader())
                    .setPageDataFetcher(new BufferedPageDataFetcher())
                    .build();
            crawler.startCrawling(startingUrl , maxAmountOfUrls, crawlDepth, crossLevelUniqueness);
            crawler.waitCrawling();
            crawler.closeCrawler();
        }catch(IllegalArgumentException e){
            System.out.println(WRONG_USAGE_MSG);
        }
    }

    /**
     * extract and verifies the needed arguments for the program from String list.
     * @param args list of Strings from which to extract the arguments.
     */
    private static void extractArgs(String[] args) {
        if(args.length == NEEDED_ARGS_AMOUNT){
            startingUrl = args[0];
            maxAmountOfUrls = Integer.parseInt(args[1]);
            crawlDepth = Integer.parseInt(args[2]);
            crossLevelUniqueness = Boolean.parseBoolean(args[3]);
        } else {
            throw new IllegalArgumentException();
        }

    }
}



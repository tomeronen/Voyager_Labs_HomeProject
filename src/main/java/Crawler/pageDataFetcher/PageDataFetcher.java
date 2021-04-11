package Crawler.pageDataFetcher;

import java.io.IOException;

/**
 * An interface for classes the fetch data from url's.
 */
public interface PageDataFetcher {


    /**
     * Fetches data from url.
     * @param url the url to fetch data from.
     * @return the data fetched from url.
     * @throws IOException if there was a problem fetching data from url.
     */
    String fetchData(String url) throws IOException;
}

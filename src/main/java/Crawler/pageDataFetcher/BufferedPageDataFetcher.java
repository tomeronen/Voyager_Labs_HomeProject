package Crawler.pageDataFetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * A PageDataFetcher That uses a buffer when fetching data.
 */
public class BufferedPageDataFetcher implements PageDataFetcher {

    /**
     * Fetches data from url.
     * @param url the url to fetch data from.
     * @return the data fetched from url.
     * @throws IOException if there was a problem fetching data from url.
     */
    @Override
    public String fetchData(String url) throws IOException {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        return response.toString();
    }

}

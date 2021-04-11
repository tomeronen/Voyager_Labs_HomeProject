package Crawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;


/**
 * A class of Static methods, used to interact with Http links.
 */
public class HttpLink {

    private final static String  FILE_TYPE = ".html";
    private final static Pattern PROTOCOL_PATTERN = Pattern.compile("^(http[s]?://www\\.|http[s]?://|www\\.)");
    private final static Pattern INVALID_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\-]");
    private static final Pattern EMPTY_PATTERN =  Pattern.compile("[/?]+$");

    /**
     * Checks if the given url is a valid url for crawling.
     * @param urlString the url to be checked.
     * @return true if url can be crawled, false otherwise.
     */
    public static boolean isCrawlable(String urlString){
            try {
                URL url = new URL(urlString);
                url.toURI();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                return httpURLConnection != null &&
                        httpURLConnection.getResponseCode() == 200 &&
                        httpURLConnection.getContentType() != null &&
                        httpURLConnection.getContentType().contains("text/html");
            } catch (URISyntaxException | IOException exception) {
                return false;
            }
    }

    /**
     * Turns url to a format which you can use as a file name.
     * @param urlString the url to find its file name format.
     * @return the file name format of the url.
     */
    public static String getUrlFileNameStringFormat(String urlString){
        String cleanedName = cleanUrl(urlString);
        return cleanedName + FILE_TYPE;
    }

    /**
     * clean url from Invalid and unwanted chars.
     * @param urlString the url to be cleaned.
     * @return the cleaned String.
     */
    private static String cleanUrl(String urlString) {
        String cleanedName = trimProtocol(urlString);
        cleanedName = removeEmptyPath(cleanedName);
        cleanedName = removeInvalidChars(cleanedName);
        return cleanedName;
    }

    /**
     * Removes an empty path from the url.
     * @param urlString the url to be cleaned.
     * @return the cleaned String.
     */
    private static String removeEmptyPath(String urlString) {
        return EMPTY_PATTERN.matcher(urlString).replaceFirst("");
    }

    /**
     * Removes Invalid chars from the url.
     * @param urlString the url to be cleaned.
     * @return the cleaned String.
     */
    private static String removeInvalidChars(String urlString) {
        return INVALID_CHARS_PATTERN.matcher(urlString).replaceAll("_");
    }

    /**
     * Removes Invalid Protocol from the url.
     * @param urlString the url to be cleaned.
     * @return the cleaned String.
     */
    private static String trimProtocol(String urlString) {
        return PROTOCOL_PATTERN.matcher(urlString).replaceFirst("");
    }
}

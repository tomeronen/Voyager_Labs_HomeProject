package Crawler.pageDataDownloader;

import Crawler.HttpLink;

import java.io.*;

public class SaveToFileDataDownloader implements PageDataDownloader {

    /**
     * download data.
     * @param content content to be downloaded.
     * @param url the url of the content.
     * @param depth the depth of the url.
     */
    @Override
    public void downloadDataFromString(String content, String url, int depth){
            String fileName  = HttpLink.getUrlFileNameStringFormat(url);
            this.saveStringToFile(content, String.valueOf(depth),  fileName);
    }


    /**
     * Saves String to File.
     * @param content The content to save in file.
     * @param directoryName the directory in which to save file.
     * @param fileName the file name in which to save the content.
     */
    private void saveStringToFile(String content, String directoryName, String fileName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(directoryName + "/" + fileName, true));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

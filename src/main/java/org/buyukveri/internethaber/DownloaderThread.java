/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.internethaber;

import java.io.FileWriter;
import org.buyukveri.common.TextCleaner;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author galip
 */
public class DownloaderThread implements Runnable {

    private String url, outputPath, filename;
    private FileWriter fwerr;

    public DownloaderThread(String url, String outputPath, String filename) {
        this.url = url;
        this.outputPath = outputPath;
        this.filename = filename;
        try {
            fwerr = new FileWriter(outputPath + "/error.txt");
        } catch (Exception e) {
        }
    }

    public void parseNewsPage(String url, String path, String filename) {
        try {
            filename = filename.substring(0, filename.indexOf("_"));

            Document doc = WebPageDownloader.getPage(url);

            Element e = doc.getElementsByAttributeValue("class", "whc").first();
            String tur = e.getElementsByAttributeValue("itemprop", "title").last().text();
            tur = TextCleaner.cleanTurkishChars(tur).replaceAll(" ", "_");

            filename = filename + "_" + tur + ".txt";

            FileWriter fw = new FileWriter(path + "/" + filename, true);

            Element news = doc.getElementsByAttributeValue("itemprop", "articleBody").first();
//            System.out.println(news.text());
            Element date = doc.getElementsByAttributeValue("itemprop", "datePublished").first();
            String dt = date.attr("content");
//            System.out.println(dt + " ;& " + news.text() + "\n");
            fw.write(dt + " ;& " + news.text() + "\n");
            fw.flush();
        } catch (Exception e) {
            try {
                System.out.println(url);
                System.out.printf(e.getMessage());
                fwerr.write(url + "\n");
                fwerr.flush();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName());
            parseNewsPage(url, outputPath, filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

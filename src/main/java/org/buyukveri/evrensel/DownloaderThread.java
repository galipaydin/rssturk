/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.evrensel;

import java.io.File;
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
    FileWriter err;

    public DownloaderThread(String url, String outputPath, String filename) {
        this.url = url;
        this.outputPath = outputPath;
        this.filename = filename;
        try {
            err = new FileWriter(new File(outputPath + "/err.txt"), true);
        } catch (Exception e) {
        }
    }

    public void downloadNews(String url, String outputPath, String filename) {
        try {
            System.out.println("Downloading " + url);
            Document doc = WebPageDownloader.getPage(url);

            String cat = "nocat";
            if (doc.getElementsByAttributeValue("class", "cats").size() > 0) {
                cat = doc.getElementsByAttributeValue("class", "cats").first().text();
                cat = TextCleaner.cleanTurkishChars(cat);
                cat = cat.replaceAll(" ", "");
            }

            Element e = doc.getElementsByAttributeValue("class", "articledate").first();
            String date = e.text();
            String haber = doc.getElementById("metin").text();
//            System.out.println(date);

            FileWriter fw = new FileWriter(outputPath + "/" + cat + ".txt", true);
            fw.write(date + ";&" + haber + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            try {
                System.out.println(url);
                err.write(url + "\n");
                err.flush();
                
            } catch (Exception ex) {
            }
//            e.printStackTrace();
        }
    }

    public void run() {
        try {
//            System.out.println(Thread.currentThread().getName());
            downloadNews(url, outputPath, filename);
            err.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

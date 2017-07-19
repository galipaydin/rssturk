/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.iha;

import org.buyukveri.mynet.*;
import java.io.FileWriter;
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
            fwerr = new FileWriter(outputPath + "/error.txt", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseNewsPage(String url, String outputPath, String fileName) {
        try {
            
            String fname = outputPath + "/" + filename;
//            String fname = outputPath + "/" + System.currentTimeMillis() + "_" +  filename;
            FileWriter fw = new FileWriter(fname, true);
            Document doc = WebPageDownloader.getPage(url);

//            Element e = doc.getElementsByAttributeValueContaining("class", "nw-news-detail-content").first();
//            System.out.println(e.toString());
            Element e = doc.getElementById("haber-detay");

            String news = e.getElementById("contextual").text();
            fw.write(news + "\n");
            fw.flush();
        } catch (Exception e) {
            System.out.println("MSG    = " + e.getMessage());
            try {
                System.out.println("ERROR = " + url);
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

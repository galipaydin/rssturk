/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.mynet;

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
//            System.out.println(doc);
            Element e = doc.getElementsByAttributeValue("itemprop", "articleBody").first();
//            System.out.println(e.toString());
            
            String news = e.text();
//            System.out.println(news);
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
                ex.printStackTrace();
            }
            e.printStackTrace();
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

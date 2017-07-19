/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.sol;

import org.buyukveri.haber10.*;
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

            String cat = doc.getElementsByAttributeValueContaining("class", "tags").first().text();

            cat = TextCleaner.cleanTurkishChars(cat);
            cat = cat.replaceAll(" ", "_").toLowerCase();

            if (!cat.equals("tum_yazilari")) {
                filename = filename + "_" + cat + ".txt";
                FileWriter fw = new FileWriter(path + "/" + filename, true);

                String text = doc.getElementById("neytivcontent").text();
                text = text.replaceAll("\n", "");
                fw.write(text + "\n");
                fw.flush();
                fw.close();
                fwerr.close();
            }
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

    public void haberPage(String url, int year, int month) {
        try {
            System.out.println(url);

        } catch (Exception e) {
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

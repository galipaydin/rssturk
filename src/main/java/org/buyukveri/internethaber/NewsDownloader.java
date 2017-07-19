/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.internethaber;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.buyukveri.common.TextCleaner;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author galip
 */
public class NewsDownloader {

    public void processLinkFolder(String linkFilesFolder, String outputFolder) {
        try {
            File f = new File(linkFilesFolder);
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    readLinkFile(file, outputFolder);
                }
            }
        } catch (Exception e) {
        }
    }

     public void readLinkFile(File inputFile, String outputPath) {
        try {
            String filename = inputFile.getName();

            File f = new File(outputPath);
            if (!f.exists()) {
                f.mkdirs();
            }

            ExecutorService executor = Executors.newFixedThreadPool(10);
            
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                Runnable worker = new org.buyukveri.internethaber.DownloaderThread(line, outputPath, filename);
                executor.execute(worker);
            }

            System.out.println("Finished all threads");

            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println("filename = " + filename);

            FileWriter fw = new FileWriter(path + "/" + filename, true);

            Element news = doc.getElementsByAttributeValue("itemprop", "articleBody").first();
//            System.out.println(news.text());
            Element date = doc.getElementsByAttributeValue("itemprop", "datePublished").first();
           String dt = date.attr("content");
//            System.out.println(dt + " ;& " + news.text() + "\n");
            fw.write(dt + " ;& " + news.text() + "\n");
            fw.flush();
        } catch (Exception e) {
            System.out.println(url);
            System.out.printf(e.getMessage());
        }
    }

    public static void main(String[] args) {
        NewsDownloader n = new NewsDownloader();
        n.processLinkFolder("/Users/galip/dev/data/internethaber/links", "/Users/galip/dev/data/internethaber/news");
//        n.parseNewsPage("http://www.internethaber.com/ciadan-halepce-itirafi-1000742h.htm", "");
    }
}

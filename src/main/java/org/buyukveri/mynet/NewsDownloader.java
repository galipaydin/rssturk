/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.mynet;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
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

            FileWriter fw = new FileWriter(outputPath + "/" + filename);
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                String url = "http://finans.mynet.com" + line;
                System.out.println(url);
                parseNewsPage(url, fw);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void parseNewsPage(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Element e = doc.getElementById("haber-detay");
            String news = e.getElementsByTag("div").first().text();
            fw.write(news + "\n");
            fw.flush();
        } catch (Exception e) {
            System.out.println(url);
            System.out.printf(e.getMessage());
        }
    }

    public static void main(String[] args) {
        NewsDownloader n = new NewsDownloader();
//        n.parseNewsPage("http://finans.mynet.com/haber/detay/analiz/altin-analiz/122070/", null);
        n.processLinkFolder("/Users/galip/dev/data/mynet", "/Users/galip/dev/data/mynet/news");
    }
}

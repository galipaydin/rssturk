/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.internethaber;

import java.io.File;
import java.io.FileWriter;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class LinkExtractor {

    private FileWriter fw;

    public void makeLinks(String folder) {
        try {
           File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
            
            String url = "http://www.internethaber.com/arsiv";
//        http://www.internethaber.com/arsiv/2003/1
            for (int i = 2003; i < 2018; i++) {
                for (int j = 1; j < 13; j++) {
                    String filename = i + "_" + j + ".txt";
                    fw = new FileWriter(folder + "/" + filename);
                    String link = url + "/" + i + "/" + j;
                    System.out.println("link = " + link);

                    monthIndexPagination(link, fw, 1);

                    fw.flush();
                    fw.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void monthIndexPagination(String url, FileWriter fw, int start) {
        try {
            if (url.contains("?page=")) {
                url = url.substring(0, url.indexOf("=") + 1) + start;
//                System.out.println("++ " + url);
            }

            Document doc = WebPageDownloader.getPage(url);

            Element pagination = doc.getElementsByAttributeValueContaining("class", "pnav").first();
            Elements pages = pagination.getElementsByTag("a");
            Element last = pages.get(pages.size() - 2);
//            System.out.println(last.text());
            int lastp = Integer.parseInt(last.text());

            for (int i = start; i <= lastp; i++) {
                String link = "";
                if (url.contains("?page=")) {
                    link = url.substring(0, url.indexOf("=") + 1) + +i;
//                    System.out.println("++ " + url);
                } else {
                    link = url + "?page=" + i;
                }
                System.out.println(link);
                monthLinks(link, fw);

                if (i == lastp) {
//                    System.out.println("**" + link);
                    monthIndexPagination(link, fw, lastp + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void monthLinks(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);

            Element content = doc.getElementsByAttributeValueContaining("class", "wrap").first();
            Element list = content.getElementsByAttributeValueContaining("class", "list").first();
            Elements links = list.getElementsByTag("a");
            String a = "", b = "";
            for (Element link : links) {

                String lnk = link.attr("href");
                if (lnk.endsWith(".htm")) {
                    if (!lnk.equals(a)) {
                        System.out.println("\t" + lnk);
                        fw.write(lnk + "\n");
                        fw.flush();
                        a = lnk;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void month(int year, int month, String folder){
         try {
            String url = "http://www.internethaber.com/arsiv";
//        http://www.internethaber.com/arsiv/2003/1
                    String filename = year + "_" + month + ".txt";
                    fw = new FileWriter(folder + "/" + filename);
                    String link = url + "/" + year + "/" + month;
                    System.out.println("link = " + link);

                    monthIndexPagination(link, fw, 1);

                    fw.flush();
                    fw.close();
                
            
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        LinkExtractor l = new LinkExtractor();
        l.makeLinks("/Users/galip/dev/data/news/internethaber/links");
//        l.monthlyNewsPage("http://www.internethaber.com/arsiv/2013/4");
//        l.month(2003, 1, "/Users/galip/dev/data/internethaber/links");
    }
}
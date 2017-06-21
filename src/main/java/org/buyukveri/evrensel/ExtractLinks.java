/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.evrensel;

import java.io.FileWriter;
import java.util.Calendar;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class ExtractLinks {

    String folder;

    public void makeLinks(String folder) {
        try {
            this.folder = folder;
//            FileWriter fw = new FileWriter(folder);
            String url = "https://www.evrensel.net/arsiv";
//        https://www.evrensel.net/arsiv/20000101/tarih
            for (int i = 2000; i < 2018; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                for (int j = 1; j < 13; j++) {
                    calendar.set(Calendar.MONTH, j);
                    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int k = 1; k <= daysInMonth; k++) {
                        String day = k + "", month = j + "";

                        if (j <= 9) {
                            month = "0" + j;
                        }
                        if (k <= 9) {
                            day = "0" + k;
                        }
//                        folder = folder + "/" + i + "_" + month;
//                        System.out.println("folder = " + folder);
                        String link = url + "/" + i + "" + month + "" + day + "/tarih";
                        System.out.println(link);
                        day(link, i + "_" + month);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void day(String url, String date) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            if (!doc.getElementsByAttributeValueContaining("class", "page-numbers").text().equals("")) {
                Element pagination = doc.getElementsByAttributeValueContaining("class", "page-numbers").first();
                Elements pages = pagination.getElementsByTag("a");
                System.out.println(pages.size());
                if (pages.size() > 1) {
                    int last = Integer.parseInt(pages.get(pages.size() - 2).text());
                    for (int i = 1; i <= last; i++) {
                        String link = url + "/s/" + i;
                        dayLinks(link, date);
                    }
                }
            } else {
                dayLinks(url, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dayLinks(String url, String date) {
        try {
//            System.out.println(folder + "/links/" + date + ".txt");
            FileWriter fw = new FileWriter(folder + "/links/" + date + ".txt", true);
            Document doc = WebPageDownloader.getPage(url);
            Elements items = doc.getElementsByAttributeValue("class", "item-category");
            for (Element item : items) {
                String link = item.getElementsByTag("a").first().attr("href");
//                System.out.println("link = " + link);
                fw.write(link+"\n");
                fw.flush();
//                downloadNews(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadNews(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Element item = doc.getElementsByAttributeValue("class", "cats").first();
            String category = "nocat";
            if (item.getElementsByTag("a").size() >0) {
                category = item.getElementsByTag("a").first().text();
//                System.out.println("cat = " + category);
            }
//            else 
//                System.out.println("NOCAT");
            String text = doc.getElementById("metin").text();
            System.out.println(category);
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    public static void main(String[] args) {
        ExtractLinks e = new ExtractLinks();
        e.makeLinks("/Users/galip/dev/data/news/evrensel");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.evrensel;

import java.io.FileWriter;
import org.buyukveri.common.TextCleaner;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class Yazarlar {

    private String folder;

    public void yazarList(String folder) {
        try {
            this.folder = folder;

            for (int i = 1; i < 5; i++) {
                String url = "https://www.evrensel.net/yazarlar/s/" + i;
//                System.out.println("url = " + url);
                Document doc = WebPageDownloader.getPage(url);
                Elements ees = doc.getElementsByAttributeValue("class", "author-content");
                for (Element e : ees) {
                    String link = e.getElementsByTag("h5").first().getElementsByTag("a").first().attr("href");
//                    System.out.println("link = " + link);
                    String auth = e.getElementsByTag("h3").first().text();
//                    System.out.println("auth = " + auth);
                    yazarPage(link, auth);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yazarPage(String url, String author) {
        try {
            System.out.println("url=" + url);
            Document doc = WebPageDownloader.getPage(url);
            if (!doc.getElementsByAttributeValueContaining("class", "page-numbers").text().equals("")) {
                Element pagination = doc.getElementsByAttributeValueContaining("class", "page-numbers").first();
                Elements pages = pagination.getElementsByTag("a");
                if (pages.size() > 1) {
                    int last = Integer.parseInt(pages.get(pages.size() - 2).text());
                    for (int i = 1; i <= last; i++) {

                        String link = url + "/s/" + i;
                        System.out.println("link = " + link);
                        Document doc1 = WebPageDownloader.getPage(link);

                        Elements yazilar = doc1.getElementsByAttributeValue("class", "item");
                        for (Element yazi : yazilar) {
                            if (yazi.getElementsByAttributeValue("class", "item-content-date-yazar").size()>0) {
                                String href = yazi.getElementsByAttributeValue("class", "item-content-date-yazar").
                                        first().getElementsByTag("a").attr("href");
//                            System.out.println("\thref = " + href);
                                String date = yazi.getElementsByAttributeValue("class", "item-footer")
                                        .first().text();
//                        System.out.println("date = " + date);
                                articlePage(href, date, author);
                            }
                        }
                    }
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void articlePage(String url, String date, String author) {
        try {
            author = TextCleaner.cleanTurkishChars(author).toLowerCase().replaceAll(" ", "_");
            Document doc = WebPageDownloader.getPage(url);
            String text = doc.getElementsByAttributeValue("class", "shortcode-content").
                    first().text().replaceFirst("Tüm yazıları", "");
            FileWriter fw = new FileWriter(this.folder + "/" + author + ".txt", true);
            fw.write(date + ";&" + text + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar();
        y.yazarList("/Users/galip/dev/data/news/evrensel/yazarlar");
    }
}

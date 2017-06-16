/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.hurriyet;

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

    public Yazarlar(String folder) {
        this.folder = folder;
    }

    public void yazarLinks(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);

            Elements boxes = doc.getElementsByAttributeValueContaining("class", "author-box");
            for (Element box : boxes) {

                String href = box.attr("href");
                String link = "http://www.hurriyet.com.tr" + href;
                if (!link.endsWith("com.tr")) {
                    System.out.println(link);
                    yazarPage(link);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yazarPage(String url) {
        try {

            for (int i = 0; i < 100; i++) {
                String link = url + "/?p=" + i;
                Document doc = WebPageDownloader.getPage(link);

                if (doc.getElementsByAttributeValueContaining("class", "highlighted-box") != null) {

                    Elements boxes = doc.getElementsByAttributeValueContaining("class", "highlighted-box");
                    for (Element box : boxes) {
                        Element date = box.getElementsByAttributeValue("class", "date").first();
                        Element title = box.getElementsByAttributeValue("class", "title").first();
                        String haberlink = "http://www.hurriyet.com.tr" + title.attr("href");
                        if (!link.endsWith("com.tr")) {
//                            articlePage(haberlink, date.text());
                            articleSave(haberlink, date.text());
                        }
                    }
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void articlePage(String url, String date) {
        try {
            System.out.println(url + " " + date);
            date = date.substring(date.indexOf(" ")).trim().replaceAll(" ", "_").toLowerCase();
            date = TextCleaner.cleanTurkishChars(date);
            Document doc = WebPageDownloader.getPage(url);
            String content = doc.getElementsByAttributeValueContaining("class", "news-text").first().text();
//            System.out.println(content);
            FileWriter fw = new FileWriter(this.folder + "/" + date + ".txt", true);
            fw.write(content + "\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void articleSave(String url, String date) {
        try {
            System.out.println(url + " - " + date);
            
            date = date.replaceAll(" ", "_").toLowerCase();
//            date = date.substring(date.indexOf(" ")).trim().replaceAll(" ", "_").toLowerCase();
            date = TextCleaner.cleanTurkishChars(date);
//            System.out.println(url + " " + date);
            Document doc = WebPageDownloader.getPage(url);
            String author = doc.getElementsByAttributeValue("class", "name").first().text();
            if (author != null && author.trim().length() > 5) {
                author = author.trim().toLowerCase().replaceAll(" ", "_");
                author = TextCleaner.cleanTurkishChars(author);
//                System.out.println("author = " + author);
                String content = doc.getElementsByAttributeValueContaining("class", "news-text").first().text();
//            System.out.println(content);
                FileWriter fw = new FileWriter(this.folder + "/" + author + ".txt", true);
            fw.write(date + ";&" + content + "\n");
            fw.flush();
            fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar("/Users/galip/dev/data/hurriyet/yazarlar");
      y.yazarLinks("http://www.hurriyet.com.tr/yazarlar/tum-yazarlar/");
//      y.yazarPage("http://www.hurriyet.com.tr/yazarlar/yalcin-granit/");
//        y.articleSave("http://www.hurriyet.com.tr/3-yasinda-meme-kanseri-oldu-16726430", "");
    }
}

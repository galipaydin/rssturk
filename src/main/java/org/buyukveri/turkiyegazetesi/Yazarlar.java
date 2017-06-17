/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.turkiyegazetesi;

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

    String folder;

    public Yazarlar(String folder) {
        this.folder = folder;
    }

    void yazarList(String url) {
        Document doc = WebPageDownloader.getPage(url);
        Elements authors = doc.getElementsByAttributeValueContaining("class", "Yazarlar-sag-kutular");
        System.out.println("size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByAttributeValue("class", "YazarlarSol").first();
            String link = auth.attr("href").trim();
            System.out.println("link = " + link);
            String au = auth.text().replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
            System.out.println(au);
            yazarPage(link, au);
        }
    }

    void yazarPage(String url, String author) {
        try {
            FileWriter fw = new FileWriter(this.folder + "/" + author + ".txt", true);
            Document doc1 = WebPageDownloader.getPage(url);
            int limit = 500;
            Element pagenos = doc1.getElementsByAttributeValue("class", "sayfa_linkleri").first();
            Elements nos = pagenos.getElementsByTag("a");
            int adet = nos.size();
            if (adet > 0) {
                String lastlink = nos.last().attr("href");
                String last = lastlink.substring(lastlink.lastIndexOf("/")+1);
                limit = Integer.parseInt(last);
//                System.out.println("limit = " + limit);
            }

            for (int i = 1; i <= limit; i++) {
//                System.out.println("PAGE=" + i);
                String link = url + "/" + i;
//                System.out.println("link = " + link);
                Document doc = WebPageDownloader.getPage(link);
                int size = doc.getElementsByAttributeValueContaining("class", "yazar-teksatir").size();
                if (size > 0) {
                    Elements entries = doc.getElementsByAttributeValueContaining("class", "yazar-teksatir");
                    for (int j = 0; j < size; j+=2) {
                        Element entry = entries.get(j);
                        Element a = entry.getElementsByTag("a").first();
                        String href = a.attr("href").trim();
                        articlePage(href, fw);
                    }

                } else {
                    break;
                }
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void articlePage(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Element date = doc.getElementsByAttributeValueContaining("class", "article-date").first();
            String dt = date.text();
            String text = doc.getElementsByAttributeValueContaining("id", "article_body").first().text();

//            System.out.println(dt);
//            System.out.println(text);
            fw.write(dt + ";&" + text + "\n");
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar("/Users/galip/dev/data/news/turkiye/yazarlar");
        y.yazarList("http://www.turkiyegazetesi.com.tr/yazarlar");
    }
}

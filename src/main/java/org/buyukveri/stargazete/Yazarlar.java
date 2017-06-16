package org.buyukveri.stargazete;

import java.io.FileWriter;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author bim
 */
public class Yazarlar {

    void yazarList(String url) {
        Document doc = WebPageDownloader.getPage(url);
        FileWriter fw;

        Elements authors = doc.getElementsByAttributeValueContaining("class", "ap-author-wrapper");
        System.out.println("devamlinks.size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByAttributeValue("class", "author-name").first();
            String link = auth.attr("href").trim();
            System.out.println(link);
            System.out.println(auth.text());
            yazarPage(link);
        }
    }

    void yazarPage(String url) {
        try {
            for (int i = 1; i < 100; i++) {
                System.out.println("PAGE="+ i);
                String link = url + "/?sayfa=" + i;
                Document doc = WebPageDownloader.getPage(link);
                if (doc.getElementsByAttributeValueContaining("class", "author-blog-list") != null) {
                    Elements entries = doc.getElementsByAttributeValueContaining("class", "author-blog-list");
                    for (Element entry : entries) {
                        String href = entry.attr("href").trim();
                        System.out.println("href = " + href);
                    }
                } else {
                    System.out.println("\n******" + i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar();
        y.yazarList("http://www.star.com.tr/yazarlar/");
    }
}


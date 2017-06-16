package org.buyukveri.stargazete;

import java.io.FileWriter;
import org.buyukveri.common.TextCleaner;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Yazarlar {

    String folder;

    public Yazarlar(String folder) {
        this.folder = folder;
    }

    void yazarList(String url) {
        Document doc = WebPageDownloader.getPage(url);
        Elements authors = doc.getElementById("authorssixth-tab").
                getElementsByAttributeValueContaining("class", "ap-author-wrapper");
        System.out.println("devamlinks.size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByAttributeValue("class", "author-name").first();
            String link = auth.attr("href").trim();
            String au = auth.text().replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
            System.out.println(au);
            yazarPage(link, au);
        }
    }

    void arsivYazarlar(String url) {
        Document doc = WebPageDownloader.getPage(url);
        Element drop = doc.getElementsByAttributeValueContaining("class", "dropdown").first();
        Elements authors = drop.getElementsByTag("a");
        for (Element author : authors) {
            String name = author.text();
            String link = author.attr("href");
            String au = name.replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
           System.out.println(au );
            yazarPage(link,au);
        }
    }

    void yazarPage(String url, String author) {
        try {
            FileWriter fw = new FileWriter(this.folder + "/" + author + ".txt", true);

            for (int i = 1; i < 100; i++) {
                System.out.println("PAGE=" + i);
                String link = url + "/?sayfa=" + i;
                Document doc = WebPageDownloader.getPage(link);
                int size = doc.getElementsByAttributeValueContaining("class", "author-blog-list").size();
//                System.out.println("size = " + size);
                if (size > 0) {
                    Elements entries = doc.getElementsByAttributeValueContaining("class", "author-blog-list");
                    for (Element entry : entries) {
                        String href = entry.attr("href").trim();
                        articlePage(href, fw);
//                        System.out.println("href = " + href);
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
            Element date = doc.getElementsByAttributeValueContaining("class", "publisheddate").first();
            String dt = date.text();
            String text = doc.getElementsByAttributeValueContaining("class", "blog-detail-text").first().text();

//            System.out.println(dt);
//            System.out.println(text);
            fw.write(dt + ";&" + text + "\n");
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar("/Users/galip/dev/data/news/star/yazarlar");
        y.yazarList("http://www.star.com.tr/yazarlar/");
        y.arsivYazarlar("http://www.star.com.tr/yazarlar/");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.cumhuriyet;

import java.io.File;
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
         File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
    }

    void yazarList(String url) {
        Document doc = WebPageDownloader.getPage(url);
        Element list = doc.getElementById("tum-yazarlar");
        Elements authors = list.getElementsByAttributeValue("class", "author");
//        System.out.println("size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByTag("a").first();
            String link = "http://www.cumhuriyet.com.tr" + auth.attr("href").trim();
//            System.out.println("link = " + link);
            String au = auth.text().replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
//            System.out.println(au);
            yazarPage(link, au);
        }
    }

    void konukYazarlar(String url) {
        Document doc = WebPageDownloader.getPage(url);
        Element list = doc.getElementById("gunun-yazarlari");
        Elements authors = list.getElementsByAttributeValue("class", "author");
        System.out.println("size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByTag("a").first();
            String link = "http://www.cumhuriyet.com.tr" + auth.attr("href").trim();
            System.out.println(link);
            String au = auth.text().replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
//            System.out.println(au);
            yazarPage(link, au);
        }
    }

    void yazarPage(String url, String author) {
        try {
            FileWriter fw = new FileWriter(this.folder + "/" + author + ".txt", true);
            Document doc1 = WebPageDownloader.getPage(url);

            Element lastno = doc1.getElementsByAttributeValueEnding("class", "last").first();
            
            if (lastno.getElementsByTag("a").first() != null) {
                System.out.println("NULL");
                String link = lastno.getElementsByTag("a").first().attr("href");
//            System.out.println(link);

                String no = link.substring(0, link.lastIndexOf("/"));
                String yazarid = no.substring(0, no.lastIndexOf("/"));
                String yazar = link.substring(link.lastIndexOf("/"));
                yazarid = yazarid.substring(yazarid.lastIndexOf("/") + 1);
                no = no.substring(no.lastIndexOf("/") + 1);

            System.out.println("yazar = " + yazar);
//            System.out.println("yazarid = " + yazarid);
//            System.out.println("index=" + no);
                int index = Integer.parseInt(no);
            for (int i = 1; i <= index; i++) {
                System.out.println("PAGE = " + i);
                String newurl = "http://www.cumhuriyet.com.tr/koseyazari/" + yazarid + "/" + i + yazar;
//                System.out.println(" " + newurl);
                Document doc = WebPageDownloader.getPage(newurl);
                Element list = doc.getElementById("article-list");
                Elements as = list.getElementsByTag("a");
                for (Element a : as) {
                    String articlelink = a.attr("href");
//                    System.out.println("articlelink = " + articlelink);
                    articlePage("http://www.cumhuriyet.com.tr" + articlelink, fw);
                }
            }
            }
            else{
                Document doc = WebPageDownloader.getPage(url);
                Element list = doc.getElementById("article-list");
                Elements as = list.getElementsByTag("a");
                for (Element a : as) {
                    String articlelink = a.attr("href");
//                    System.out.println("articlelink = " + articlelink);
                    articlePage("http://www.cumhuriyet.com.tr" + articlelink, fw);
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
            Element article = doc.getElementById("article-body");
            String text = article.text();
            Element date = doc.getElementById("publish-date");
            String dt = date.text();
//            System.out.println("dt = " + dt);
//            System.out.println("text = " + text);
            fw.write(dt + ";&" + text + "\n");
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Yazarlar y = new Yazarlar("/Users/galip/dev/data/news/cumhuriyet/yazarlar");
        y.yazarList("http://www.cumhuriyet.com.tr/yazarlar");
        y.konukYazarlar("http://www.cumhuriyet.com.tr/KonukYazarlar");
    }
}

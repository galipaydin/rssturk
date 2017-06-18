/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.cumhuriyet;

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
        Element list = doc.getElementById("tum-yazarlar");
        Elements authors = list.getElementsByAttributeValue("class", "author");
        System.out.println("size() = " + authors.size());
        for (Element author : authors) {
            Element auth = author.getElementsByTag("a").first();
            String link = auth.attr("href").trim();
            System.out.println("link = " + link);
            String au = auth.text().replaceAll(" ", "_").toLowerCase();
            au = TextCleaner.cleanTurkishChars(au);
            System.out.println(au);
//            yazarPage(link, au);
        }
    }

    
    public static void main(String[] args) {
        Yazarlar y = new Yazarlar("");
        y.yazarList("http://www.cumhuriyet.com.tr/yazarlar");
    }
}

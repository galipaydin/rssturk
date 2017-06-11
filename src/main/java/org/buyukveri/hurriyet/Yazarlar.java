/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.hurriyet;

import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class Yazarlar {

    public void yazarLinks(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);

            Elements boxes = doc.getElementsByAttributeValueContaining("class", "author-box");
            for (Element box : boxes) {
                
            String href = box.attr("href");
                System.out.println(href);
            }

        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args) {
        Yazarlar y = new Yazarlar();
        y.yazarLinks("http://www.hurriyet.com.tr/yazarlar/arsiv-yazarlari/");
    }
}

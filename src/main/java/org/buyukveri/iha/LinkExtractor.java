/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.iha;

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

    private static String path = "/Users/galip/dev/data/news/iha/links";

    public void linkMaker(String url, String cat) {
        try {
            FileWriter fw = new FileWriter(path + "/" + cat + ".txt", true);
            for (int i = 1; i < 5000; i++) {
                System.out.println("PAGE=" + i);
                String link = url + cat + "/" + i + ".sayfa.html";
                Document doc = WebPageDownloader.getPage(link);
                if (doc.getElementsByAttributeValueContaining("class", "related") != null) {
                    Elements ll = doc.getElementsByAttributeValueContaining("class", "related");
                    if (ll.size() > 0) {
                        Element list = doc.getElementsByAttributeValueContaining("class", "related").first();
                        Elements boxes = list.getElementsByTag("a");
                        if (boxes.size() > 0) {
                            for (Element box : boxes) {
                                String newsurl = box.attr("href");
                                System.out.println("\t" + newsurl);
                                fw.write(newsurl + "\n");
                                fw.flush();
//                    fw.write(newsurl + "\n");
//                fw.flush();
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getLinks() {
        String[] cats = {"asayis", "cevre", "dunya", 
            "egitim","ekonomi", "gundem",
            "kultur-sanat", "magazin", 
            "spor", "politika",  "yerel-haberler"};
        String url = "http://www.iha.com.tr/";
        for (String cat : cats) {
            System.out.println(cat);
            linkMaker(url, cat);
        }
    }

    public static void main(String[] args) {
        LinkExtractor n = new LinkExtractor();
//        n.parseIndexPage("http://finans.mynet.com/haber/arsiv/25/5/2011/borsa/", null);
//         n.dateMaker(2012, "ekonomi");
        n.getLinks();
    }

}

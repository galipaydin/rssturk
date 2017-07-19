/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.diken;

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

    private static String path = "/Users/galip/dev/data/news/diken/links";

    public void linkMaker(String cat, int last) {
        try {
            FileWriter fw;
            if (!cat.equals("agora")) {
                fw = new FileWriter(path + "/" + cat + ".txt", true);
            } else {
                fw = new FileWriter(path + "/yazarlar.txt", true);
            }

            String url = "http://www.diken.com.tr/kategori/" + cat;
            for (int i = 1; i < last; i++) {
                String link = url + "/page/" + i;
                System.out.println(link);
                Document doc = WebPageDownloader.getPage(link);
                Element content = doc.getElementsByAttributeValue("class", "content").first();
                Elements boxes = content.getElementsByAttributeValueContaining("class", "category-" + cat);
                System.out.println(boxes.size());
                int cnt = 0;
                if (boxes.size() > 0) {
                    for (Element box : boxes) {
                        Element h2 = box.getElementsByTag("a").first();
//                            System.out.println("h2 = " + h2);
                        String newsurl = h2.attr("href");
                        System.out.println(cnt++ + "-" + newsurl);
                        fw.write(newsurl + "\n");
                        fw.flush();

                    }
                } else {
                    System.out.println("BREAK");
                    break;
                }

            }

            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getLinks() {
//        String[] cats = { };
        String[] cats = {"agora", "diken-ozel", "spor", "dunya", "medya", "keyif", "aktuel",
            "analiz", "dikene-takilanlar", "diken11", "vpn-haber"};
        for (String cat : cats) {
            System.out.println(cat);
            linkMaker(cat, 1000);
        }
    }

    public static void main(String[] args) {
        LinkExtractor n = new LinkExtractor();
        n.getLinks();
    }
}

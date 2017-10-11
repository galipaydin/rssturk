/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.sol;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class LinkExtractor {

    private String folder = "/Users/galip/dev/data/news/sol/links";
    private FileWriter fw;

    public LinkExtractor() {
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdirs();
        }

    }

    public void pagination(String url, String cat) {
        try {

            Document doc = WebPageDownloader.getPage(url);
            Element pagination = doc.getElementsByAttributeValueContaining("class", "pager-last").first();
            Element a = pagination.getElementsByTag("a").first();
            String href = a.attr("href");

            FileWriter fw = new FileWriter(folder + "/" + cat + "_links.txt");

            int last = Integer.parseInt(href.substring(href.indexOf("=") + 1));
            System.out.println(last);
            for (int i = 1; i < last; i++) {
                String link = url + "?page=" + i;
                System.out.println(link);
                newsLink(link, fw);
            }
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newsLink(String url, FileWriter fw) {
        try {

            Document doc = WebPageDownloader.getPage(url);

            Elements boxes = doc.getElementsByAttributeValueContaining("class", "allcategorynews-title");

            for (Element box : boxes) {
                Element a = box.getElementsByTag("a").first();
                String haberLink = a.attr("href");
                System.out.println(haberLink);
                fw.write("http://haber.sol.org.tr" + haberLink + "\n");
                fw.flush();
//                haberPage(haberLink, year, month);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        LinkExtractor l = new LinkExtractor();
        l.pagination("http://haber.sol.org.tr/kategori/turkiye", "turkiye");
        l.pagination("http://haber.sol.org.tr/kategori/emek-sermaye", "emek");
        l.pagination("http://haber.sol.org.tr/kategori/dunya", "dunya");
        l.pagination("http://haber.sol.org.tr/kategori/kultur-sanat", "kultur-sanat");
        l.pagination("http://haber.sol.org.tr/kategori/toplum", "toplum");
        l.pagination("http://haber.sol.org.tr/kategori/medya", "medya");
        l.pagination("http://haber.sol.org.tr/kategori/spor", "spor");
    }
}

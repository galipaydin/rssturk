/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.mynet;

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

   private static String path = "/Users/galip/dev/data/mynet";
 
    
    public void extractLinks(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Elements boxes = doc.getElementsByAttributeValueContaining("class", "fmNewsBox");
            for (Element box : boxes) {
                String newsurl = box.getElementsByTag("a").attr("href");
                fw.write(newsurl + "\n");
                            fw.flush();

//                System.out.println("\t" + newsurl);
            }
            fw.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void dateMaker(int year, String cat) {
        try {
            FileWriter fw = new FileWriter(path + "/" + year + "_" + cat + ".txt");
            String url = "http://finans.mynet.com/haber/arsiv/";
//        + "25/5/2011"
//        + "/otomotiv/";
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            for (int month = 0; month < 12; month++) {
                calendar.set(Calendar.MONTH, month);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int k = 1; k <= daysInMonth; k++) {
                    url += k + "/" + (month + 1) + "/" + year + "/" + cat;
                    System.out.println(url);
                    extractLinks(url, fw);
                    url = "http://finans.mynet.com/haber/arsiv/";
                }
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLinks() {
        String[] cats = {"borsa", "doviz", "ekonomi", "dunya", "emlak", "otomotiv", "turizm", "analiz"};
        for (int i = 2011; i < 2018; i++) {
            for (String cat : cats) {
                dateMaker(i, cat);
            }
        }
    }

    public static void main(String[] args) {
        LinkExtractor n = new LinkExtractor();
//        n.parseIndexPage("http://finans.mynet.com/haber/arsiv/25/5/2011/borsa/", null);
//         n.dateMaker(2012, "ekonomi");
        n.getLinks();
    }

 
}

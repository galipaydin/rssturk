/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.haber10;

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

    private String folder = "/Users/galip/dev/data/news/haber10/links";
    private FileWriter fw;

    public void dateMaker() {
        try {
//        http://www.haber10.com/arsiv/2005/08/03
            String url = "http://www.haber10.com/arsiv/";
            for (int year = 2005; year < 2007; year++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                for (int month = 0; month < 12; month++) {
                    calendar.set(Calendar.MONTH, month);
                    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int day = 1; day <= daysInMonth; day++) {
                        url += year + "/" + (month + 1) + "/" + day;
                        System.out.println(url);
                        dayIndexPagination(url, year, month);
                        url = "http://www.haber10.com/arsiv/";
                    }
                }
            }

//            fw.flush();
//            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dayIndexPagination(String url, int year, int month) {
        try {
            Document doc = WebPageDownloader.getPage(url);
                Element pagination = doc.getElementsByAttributeValueContaining("class", "pagination").first();
                Elements pages = pagination.getElementsByTag("a");
            if (pages.size() >0 ) {
                Element last = pages.get(pages.size() - 2);
                if (last != null) {
                    int lastp = 0;
                    lastp = Integer.parseInt(last.text());
                    System.out.println("lastp = " + lastp);
                    if (lastp > 0) {
                        for (int i = 1; i <= lastp; i++) {
                            String link = url + "?page=" + i;
                            System.out.println("\t" + link);
                            dayLink(link, year, month);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dayLink(String url, int year, int month) {
        try {
            FileWriter fw = new FileWriter(folder + "/" + year + "_" + ++month + ".txt", true);

            Document doc = WebPageDownloader.getPage(url);

            Elements boxes = doc.getElementsByAttributeValueContaining("class", "one-block");

            for (Element box : boxes) {
                Element a = box.getElementsByTag("a").first();
                String haberLink = a.attr("href");
//                System.out.println(haberLink);
                fw.write(haberLink + "\n");
                fw.flush();
//                haberPage(haberLink, year, month);
            }
            fw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        LinkExtractor l = new LinkExtractor();
        l.dateMaker();
    }
}

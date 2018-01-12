
package org.buyukveri.sabah;

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

    private static String path = "/Users/galip/dev/data/news/sabah";

    public void extractLinks(String url, FileWriter fw) {
        try {
            for (int i = 1; i < 100; i++) {
                Document doc = WebPageDownloader.getPage(url + "?page=" + i);
//                System.out.println("doc.title() = " + doc.title());
                Elements boxes = doc.getElementsByAttributeValueContaining("class", "innerItem");
                if (boxes != null && boxes.size() > 0) {
                    for (Element box : boxes) {
                        String newsurl = box.getElementsByTag("a").attr("href");
//                        System.out.println("\t" + newsurl);
                        if (!newsurl.trim().equals("")) {
                            fw.write(newsurl + "\n");
                            fw.flush();
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void dateMaker() {
        try {
            String url = "http://www.sabah.com.tr/timeline/"; //2009/01/01";
            Calendar calendar = Calendar.getInstance();

            for (int year = 2009; year < 2018; year++) {
            FileWriter fw = new FileWriter(path + "/links_"+ year+".txt");
                calendar.set(Calendar.YEAR, year);
                for (int month = 0; month < 12; month++) {
                    calendar.set(Calendar.MONTH, month);
                    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int k = 1; k <= daysInMonth; k++) {
                        url += year + "/" + (month + 1) + "/" + k;
//                        System.out.println(url);
                        extractLinks(url, fw);
                        url = "http://www.sabah.com.tr/timeline/";
                    }
                }
                fw.flush();
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LinkExtractor n = new LinkExtractor();
//        n.parseIndexPage("http://finans.mynet.com/haber/arsiv/25/5/2011/borsa/", null);
//         n.dateMaker(2012, "ekonomi");
//        n.getLinks();
        n.dateMaker();
    }

}

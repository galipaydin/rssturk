/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.haberlercom;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author bim
 */
public class NewsParser  implements Runnable {

    private String path, downloadcategory;
    private int downloadyear;
    
    private String[] categories = {"ekonomi", "guncel", "dunya", "spor", "politika",
            "yasam", "kultur-sanat", "magazin"};
    private String[] months = {"Ocak", "Subat", "Mart", "Nisan", "Mayis", "Haziran",
            "Temmuz", "Agustos", "Eylul", "Ekim", "Kasim", "Aralik"};

    public NewsParser(String path) {
            this.path = path;
    }    
    
    public NewsParser(String path, String category, int year) {
            this.path = path;
            this.downloadcategory = category;
            this.downloadyear = year;
    }

    public void parseIndexPage(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Elements boxes = doc.getElementsByAttributeValueContaining("id", "newsboxs");
            for (Element box : boxes) {
                String newsurl = box.getElementsByTag("a").attr("href");
//                System.out.println(newsurl);
                parseNewsPage(newsurl, fw);
            }

        } catch (Exception e) {
            System.out.println (e.getMessage () );
        }
    }

    public void pagination(String url, FileWriter fw) {
        try {

            Document doc = WebPageDownloader.getPage(url);
            Element slider = doc.getElementsByAttributeValueContaining("class", "slider-nav").first();
            if (slider != null) {
                if (slider.getElementsByTag("a") != null) {
                    Elements hrefs = slider.getElementsByTag("a");
                    for (int i = 2; i < hrefs.size(); i++) {
                        Element href = hrefs.get(i);
                        if (!href.toString().contains("slide-")) {
                            String newsurl = href.attr("href");
//                            System.out.println(i + " " + newsurl);
                            parseIndexPage(newsurl, fw);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println (e.getMessage () );
        }
    }


    public void downloadNews(String category, int year, int startMonth, int endMonth, String folderPath) {
        try {
            for (int month = startMonth; month <= endMonth; month++) {
                String fileName = folderPath + "/" + year + "_" + (month + 1) + ".txt";
                File f = new File(fileName);
                FileWriter fw = new FileWriter(f);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int k = 1; k <= daysInMonth; k++) {
                    System.out.println(Thread.currentThread().getName() + ":" + year + "-" + (month+1) + "-" + k + " " + category);
                    String url = "http://www.haberler.com/" + year + "/" + months[month] + "/" + k + "/" + category;
//                            System.out.println("\n-----" + url);
                    //parse first page
                    parseIndexPage(url, fw);
                    //parse next pages for the same day
                    pagination(url, fw);
                }
                fw.close();
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            System.out.println (e.getMessage () );
        }
    }

    public void parseNewsPage(String url, FileWriter fw) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            String baslik = "";
            if (doc.getElementById("haber_baslik") != null) {
                baslik = doc.getElementById("haber_baslik").text();
            }
//            System.out.println(baslik);

            Element ust = doc.getElementsByAttributeValueContaining("class", "ustblkgenislet2 spot ").first();
            String usttext = ust.text();
//            System.out.println(usttext);

            Elements els = doc.getElementsByAttributeValueContaining("class", "haber_metni");
            Element news = els.first();
            String text = news.text();

            String out = "";
            if (text.trim().length() > 10) {
//                out = baslik + "\n" + text;
                out = baslik + ";&" + text.trim ().replaceAll ("\n","");
            } else {
//                out = baslik + "\n" + usttext;
                out = baslik + ";&" + usttext.trim ().replaceAll ("\n","");

            }

            fw.write(out + "\n");
            fw.flush();

        } catch (Exception e) {
            System.out.println(url);
            System.out.printf (e.getMessage ());
        }
    }

    public void downloadAll() {
        try {
            for (int cat = 0; cat < categories.length; cat++) {
                for (int year = 2009; year <= 2017; year++) {

                    String folderPath = this.path + "/" + year + "/" + categories[cat] + "/";
                    System.out.println("folderPath = " + folderPath);
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    downloadNews(categories[cat], year, 0, 11, folderPath);

                    Thread.sleep(60000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadYear(int year) {
        try {
            for (int cat = 0; cat < categories.length; cat++) {
                    String folderPath = this.path + "/" + year + "/" + categories[cat] + "/";
                    System.out.println("folderPath = " + folderPath);
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    downloadNews(categories[cat], year, 0, 11, folderPath);
                    Thread.sleep(60000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    public void downloadYear(int year, String cat) {
        try {
                    String folderPath = this.path + "/" + year + "/" + cat + "/";
                    System.out.println("folderPath = " + folderPath);
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    downloadNews(cat, year, 0, 11, folderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    public void download(String category, int year, int startMonth, int endMonth) {
        String folderPath = this.path + "/" + year + "/" + category + "/";
        System.out.println("folderPath = " + folderPath);
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        downloadNews(category, year, startMonth - 1, endMonth - 1, folderPath);
    }

  public void run() {
        try {
            System.out.println(Thread.currentThread().getName());
            downloadYear(this.downloadyear, this.downloadcategory);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
  
    public static void main(String[] args) {

     System.out.println("Usage: NewsParser folderPath category year startMonth endMonth");
        String folderPath, category, year, startMonth, endMonth;
        if (args.length != 5) {
            System.out.println("Invalid no of parameters");
        } else {
            folderPath = args[0];
            NewsParser n = new NewsParser(folderPath);
            category = args[1];
            year = args[2];
            startMonth = args[3];
            endMonth = args[4];
            n.download(category, Integer.parseInt(year), Integer.parseInt(startMonth), Integer.parseInt(endMonth));
        }

//       NewsParser n = new NewsParser("/Users/galip/dev/data/news");
//        n.download("ekonomi", 2005, 1, 1);
    }
}

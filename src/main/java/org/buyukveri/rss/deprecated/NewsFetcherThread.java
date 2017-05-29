/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.rss.deprecated;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import java.io.File;
import java.io.IOException;

import java.util.Properties;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Galip
 */
public class NewsFetcherThread implements Runnable {

    Properties p;
    String dir;
    String[] newspapers;
    String RSSUrl, newspaper, cat;

    public NewsFetcherThread(String url, String nws, String ctg) {
        try {
            System.out.println("---RSS NEWS FETCHER---");
            p = new Properties();
            p.load(new FileReader("/Users/galip/NetBeansProjects/NewsDownloader/src/main/resources/news.properties"));
//        p = PropertyLoader.loadProperties("news");
            this.dir = p.getProperty("newsfilesdir");
            this.RSSUrl = url;
            this.newspaper = nws;
            this.cat = ctg;
        } catch (Exception ex) {
            Logger.getLogger(NewsFetcherThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTime(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
            sdf.format(date);
            String time = sdf.format(c.getTime());
            return time;
        } catch (Exception e) {
            System.out.println("ERROR-getTime: " + e.getMessage());
            return "";
        }
    }

    /**
     * Haberin tarihini kullanarak bu haberin saklanacağı dosya ismi
     * oluşturuluyor
     *
     * @param date
     * @return
     */
    public String getFileName(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy-HHmmss");
            sdf.format(date);
            String filename = sdf.format(c.getTime());
            return filename;
        } catch (Exception e) {
            System.out.println("ERROR-getFileName: " + e.getMessage());
            return "";
        }
    }

    public void parseRSS() {
        String link = "";
        try {
            URL feedUrl = new URL(RSSUrl);
            SyndFeedInput input = new SyndFeedInput();

            //"Server returned HTTP response code: 403 for URL" hatasından kurtulmak için
            System.setProperty("http.agent", "Mozilla/5.0");

            SyndFeed feed = input.build(new XmlReader(feedUrl));
            System.out.println("Haber sayısı: " + feed.getEntries().size());

            System.out.println(newspaper + "-" + cat);
            org.jdom.Document doc = new MyWireFeedInput().getDocument(new XmlReader(feedUrl));
            //org.jdom.Element channel = doc.getRootElement().getChild("channel");
            org.jdom.Element channel = (org.jdom.Element) doc.getRootElement().getChildren().get(0);
         
            SyndEntry firstItem = (SyndEntry) feed.getEntries().get(0);
            String firstDate = getFileName(firstItem.getPublishedDate());

            String filePath = this.dir + "/" + newspaper + "/" + cat + "/haberler/" + cat + "_" + firstDate + ".txt";
            File f = new File(filePath);

            if (!f.exists()) {
                List<org.jdom.Element> items = channel.getChildren("item");
                System.out.println("***" + items.size());

                Iterator entryIter = feed.getEntries().iterator();
                int i = 0, j = 1;
                while (entryIter.hasNext()) {
                    
                    System.out.print(" " + j++);
                    SyndEntry entry = (SyndEntry) entryIter.next();

                    link = entry.getLink();

                    if (newspaper.equals("turkiye")) {
                        //http://www.turkiyegazetesi.com.tr/yasam/426325.aspx
                        String ctg = link.substring(link.lastIndexOf("_") + 1, link.lastIndexOf("."));
                        ctg = ctg.substring(0, ctg.lastIndexOf("/"));
                        ctg = ctg.substring(ctg.lastIndexOf("/") + 1);
                        cat = ctg;
                        NewsFetcher.makeDirs(newspaper, cat);
                    }
//                    System.out.println("link = " + link);

                    if (entry.getCategories().size() > 0) {
                        SyndCategoryImpl s = (SyndCategoryImpl) entry.getCategories().get(0);
                        cat = s.getName().toLowerCase();
                        System.out.println("cat = " + cat);
                        if (cat.contains(",")) {
                            cat = cat.substring(0, cat.indexOf(","));
                        }
                        cat = cat.replaceAll(",", "");
                        cat = cat.replaceAll("_", "");
                        cat = cat.replaceAll("-", "");
                        cat = cat.replaceAll("-", "");
                        //cat = cat.replaceAll(">", "");
                        cat = cat.replaceAll("ı", "i");
                        cat = cat.replaceAll("ü", "u");
                        cat = cat.replaceAll("ö", "o");
                        cat = cat.replaceAll("ğ", "g");
                        cat = cat.replaceAll("ş", "s");
                        cat = cat.replaceAll("ç", "c");
                        cat = cat.trim();
                        NewsFetcher.makeDirs(newspaper, cat);
                    }

                    Date date = entry.getPublishedDate();
//                    System.out.println("DATE\t" + date);

                    String fileName = "";
                    if (date == null) {
                        fileName = cat + "_" + System.currentTimeMillis() + "";

                    } else {
                        fileName = cat + "_" + getFileName(date);
                    }

                    //DHA RSS pubDate elementi yok
                    if (newspaper.equals("dha")) {
                        String id = link.substring(link.lastIndexOf("_") + 1, link.lastIndexOf("."));
                        fileName = "dha_" + id;
                    }

                    String newsFilePath = fileName + ".txt";
                    newsFilePath = this.dir + "/" + newspaper + "/" + cat + "/haberler/" + newsFilePath;

                    String imgurl = "", imgFilePath = "";
                    if (items.size() > 0) {
                        org.jdom.Element currentElement = (org.jdom.Element) items.get(i);
                        System.out.println(currentElement.getChild("category"));

                        imgurl = JDOMParse.getImageURL(currentElement, newspaper);
                        if (imgurl.trim().length() > 0) {
                            String imgExt = imgurl.substring(imgurl.lastIndexOf("."));
                            i++;
                            imgFilePath = fileName + imgExt;
                            imgFilePath = this.dir + "/" + newspaper + "/" + cat + "/resimler/" + imgFilePath;
                        }
                    }

                    final URL linkurl = new URL(link);
                    String content = "";
                    //LargestContentExtractor.INSTANCE.getText(linkurl);
                    ArticleExtractor a = ArticleExtractor.INSTANCE;
                    
                    try {
                        if (a.getText(linkurl) != null) {
                            content = a.getText(linkurl);
                        }
                    } catch (BoilerpipeProcessingException ex) {
                        System.out.println("linkurl = " + linkurl);
                        System.out.println("parseRSS-BoilerpipeProcessingException: " + ex.getMessage());
                    }

                    if (!content.trim().equals("")) {
                        //System.out.println("newsFilePath = " + newsFilePath);
                        //System.out.println("imgFilePath = " + imgFilePath);
                        //System.out.println("imgurl = " + imgurl);
                        writeFiles(content, newsFilePath, imgFilePath, imgurl);
                    }
                }
                System.out.println("");
            } else {
                System.out.println("Yeni haber yok");
            }
        } //        catch (FeedException | IOException | IllegalArgumentException ex) { //        catch (FeedException | IOException | IllegalArgumentException ex) {
        catch (Exception ex) {
            System.out.println("link = " + link);

            System.out.println("ERROR-parseRSS: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void writeFiles(String content, String newsFilePath, String imgFilePath, String imgURL) {
        try {
            File haberFile = new File(newsFilePath);
            File resimFile = new File(imgFilePath);
            if (!haberFile.exists()) {
                try (FileWriter fw = new FileWriter(newsFilePath)) {
                    fw.write(content);
                    fw.flush();
                }
            }
            if (imgURL.trim().length() > 20) {
                if (!resimFile.exists()) {
                    try (InputStream in = new java.net.URL(imgURL).openStream();
                            OutputStream out = new BufferedOutputStream(new FileOutputStream(imgFilePath))) {
                        for (int b; (b = in.read()) != -1;) {
                            out.write(b);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR-writeFiles: " + e.getMessage());
//            System.out.println("newsFilePath = " + newsFilePath);
//            System.out.println("imgFilePath = " + imgFilePath);
//            e.printStackTrace();
        }
    }

    public void run() {
        try {
            //her bir RSS dosyası parse edilip haberler indirilir
            parseRSS();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    public final static void main(String[] args) throws Exception {
//        NewsFetcherThread z = new NewsFetcherThread();
//        z.run();
//    }
}

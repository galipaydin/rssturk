/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.rss;

import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.kohlschutter.boilerpipe.extractors.LargestContentExtractor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import org.buyukveri.common.WebPageDownloader;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class RSSParser implements Runnable {

    String category, newspaper, newsFilePath, imgFilePath, lastNewsFilePath, url;
    Properties p;
    String dir;
    private boolean firstItem, checkNew;
    private FileWriter newsFW, imgFW, lastNewsFW;

//    public RSSParser() {
//        try {
//            category = newspaper = "";
//            p = new Properties();
//            p.load(new FileReader("/Users/galip/NetBeansProjects/rssturk/src/main/resources/news.properties"));
////        p = PropertyLoader.loadProperties("news");
//            this.dir = p.getProperty("newsfilesdir");
//        } catch (Exception e) {
//        }
//    }
    public RSSParser(String link, String paper, String cat, Properties pp) {
        try {
            System.setProperty("http.agent", "Chrome");
            this.p = pp;
            this.category = this.newspaper = this.url = "";
            this.newspaper = paper;
            this.category = cat;
            this.url = link;
            this.checkNew = true;
//            p = new Properties();
//            p.load(new FileReader("/Users/galip/NetBeansProjects/rssturk/src/main/resources/news.properties"));
////        p = PropertyLoader.loadProperties("news");
            this.dir = p.getProperty("newsfilesdir");
            System.out.println("dir = " + dir);
            makeDirs(newspaper, category);
        } catch (Exception e) {
        }

    }

//    public void parser(String url, String paper, String cat) {    
    public void parser() {
        try {
//            makeDirs(this.newspaper, this.category);
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);
            String newsFileName = newspaper + "_" + day + "_" + month + "_" + year;

            lastNewsFilePath = this.dir + "/" + this.newspaper + "/" + this.category + "/lastnews.txt";
            newsFilePath = this.dir + "/" + this.newspaper + "/" + this.category + "/haberler/" + newsFileName + ".txt";
            newsFW = new FileWriter(newsFilePath, true);

            Document doc = WebPageDownloader.getPage(this.url);
            assert doc != null;
            Elements rsss = doc.getElementsByTag("rss");
            System.out.println("size: " + rsss.size());
            if (rsss.size() > 0) {
                Element rss = rsss.first();
                Element channel = rss.getElementsByTag("channel").first();
                Elements items = channel.getElementsByTag("item");

                for (Element item : items) {
                    System.out.println("processing item " + this.newspaper);
//                    System.out.println(item);
                    parseItem(item);
                    //En baştaki link daha önce indirilmişse yeni haber olmadığı
                    //için sonrakileri kontrol etme
                    if (checkNew == false) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void parseItem(Element item) {
        try {
            String ctgr, link, pubDate, imageURL, newsId;
            ctgr = link = pubDate = imageURL = newsId = "";
            newsId = System.currentTimeMillis() + "";

            Elements els = item.getAllElements();
            for (Element el : els) {
                String tagName = el.tagName();
                String value = el.text();
                if (tagName.equalsIgnoreCase("link")) {
                    link = value;
                    System.out.println("value = " + value);
                    if (checkNew) {
                        File f = new File(lastNewsFilePath);
                        if (f.exists()) {
                            Scanner s = new Scanner(f);
                            //System.out.println(s.hasNext());
                            while (s.hasNext()) {
                                String line = s.nextLine();
                                System.out.println("line = " + line);
                                if (line != null) {
                                    if (!line.trim().equals("")) {
                                        if (line.trim().equals(link)) {
                                            System.out.println("YENİ HABER YOK");
                                            checkNew = false;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!firstItem) {
                        firstItem = true;
                        lastNewsFW = new FileWriter(lastNewsFilePath);

                        lastNewsFW.write(link + "\n");
                        lastNewsFW.flush();
                        lastNewsFW.close();
                    }

                } else if (tagName.equalsIgnoreCase("category")) {
                    if (value != null) {
                        if (!value.trim().equals("")) {
                            ctgr = categoryFormatter(value);
                        }
                    }
                } else if (tagName.equalsIgnoreCase("pubDate")) {
                    pubDate = value;
                    System.out.println("date = " + pubDate);
                } else if (tagName.equalsIgnoreCase("description")) {
                    if (value.contains("img")) {
                        String imglink = value.substring(value.indexOf("src") + 5);
                        if (imglink.contains("\"")) {
                            imglink = imglink.substring(0, imglink.indexOf("\""));
                            imageURL = imglink;
                        }
                    }
                } else if (tagName.equalsIgnoreCase("image") || tagName.equalsIgnoreCase("ImageURL")) {
                    if (value != null && !value.trim().equals("")) {
                        if (value.indexOf(" ") > 0) {
                            value = value.substring(0, value.indexOf(" "));
                        }
                        imageURL = value;
                    }
                } else if (tagName.equalsIgnoreCase("media:thumbnail")
                        || tagName.equalsIgnoreCase("media:content")) {
                    imageURL = el.attr("url");
//                    System.out.println("image = " + image);
                }
            }

            if (checkNew) {
                if (ctgr.trim().equals("")) {
                    ctgr = this.category;
                }

                String imgExt = ".jpg";
                if (!imageURL.equals("") && imageURL.length() > 4) {
                    imgExt = imageURL.substring(imageURL.lastIndexOf("."));
                    if (imgExt.length() > 3) {
                        imgExt = ".jpg";
                    }
                }

                imgFilePath = this.dir + "/" + newspaper + "/" + this.category + "/resimler/" + newsId + imgExt;
                //  saveImage( imageURL,  imgFilePath);

                Document doc = WebPageDownloader.getPage(link);
                String html = doc.html();
                ArticleExtractor a = ArticleExtractor.INSTANCE;
                String content = LargestContentExtractor.INSTANCE.getText(html);
                Charset.forName("UTF-8").encode(content);
//                System.out.println(content);
                String test = a.getText(html);
                System.out.println("test = " + test);
//                if (a.getText(html) != null) {
//                    content = a.getText(html);
//                    System.out.println("****\n"+content);
//                }String date, String id, String category, String link, String content
//                String json = createJson(getTime(new Date()), newsId, ctgr, link, content);
                System.out.println(pubDate);
                String json = createJson(getTime(new Date()), newsId, ctgr, link, content);
                newsFW.write(json);
                newsFW.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void saveImage(String imageURL, String filePath) {
        try {
            File resimFile = new File(imgFilePath);

            if (imageURL.trim().length() > 20) {
                if (!resimFile.exists()) {
                    try (InputStream in = new java.net.URL(imageURL).openStream();
                            OutputStream out = new BufferedOutputStream(new FileOutputStream(imgFilePath))) {
                        for (int b; (b = in.read()) != -1;) {
                            out.write(b);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public String categoryFormatter(String cat) {
        cat = cat.toLowerCase();
        if (cat.contains(",")) {
            cat = cat.substring(0, cat.indexOf(","));
        }
        cat = cat.replaceAll(" ", "");
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
        return cat;
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

    public String createJson(String date, String id, String category, String link, String content) {
        JSONObject j = new JSONObject();
        j.put("newspaper", this.newspaper);
        j.put("category", category);
        j.put("date", date);
        j.put("id", id);
        j.put("url", link);
        j.put("content", content);

//        System.out.println(j.toString(2));
        return j.toString(3);
    }

    public void makeDirs(String newspaperName, String cat) {
        try {
            String dirPath = dir + "/" + newspaperName.toLowerCase() + "/" + cat.toLowerCase() + "/haberler";
            System.out.println("dirPath = " + dirPath);
            File f = new File(dirPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            dirPath = dir + "/" + newspaperName.toLowerCase() + "/" + cat.toLowerCase() + "/resimler";
            f = new File(dirPath);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            //her bir RSS dosyası parse edilip haberler indirilir
            System.out.println(Thread.currentThread().getName());

            parser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new RSSParser("http://www.yeniakit.com.tr/rss/haber/siyaset", "akit", "siyaset", null));
//        r.parser("http://www.milliyet.com.tr/rss/rssNew/magazinRss.xml", "milliyet", "magazin");
        t.start();
    }
}

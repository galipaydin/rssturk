package org.buyukveri.rss.deprecated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author galip
 */
public class NewsFetcher {

    Properties p;
    public static String dir;
    String[] newspaperNameler;
    int maxThreadCount;

    public NewsFetcher() {
        System.out.println("---RSS NEWS FETCHER---");
        try {
            p = new Properties();
            p.load(new FileReader("/Users/galip/NetBeansProjects/rssturk/src/main/resources/news.properties"));
//        p = PropertyLoader.loadProperties("news");
        this.dir = p.getProperty("newsfilesdir");
        this.maxThreadCount = Integer.parseInt(p.getProperty("maxThreadCount"));
        } catch (Exception ex) {
            Logger.getLogger(NewsFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void makeDirs(String newspaperName, String cat) {
        try {
            String dirPath = dir + "/" + newspaperName.toLowerCase() + "/" + cat.toLowerCase() + "/haberler";
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
            e.printStackTrace();
        }
    }

    public void fetchNewsFromRSS() {
        try {
            //RSS listelerinin olduğu dosyayı okuyarak newspaperName, kategori ve 
            //rss bilgilerini alip haberleri indirmeye başlatır
            int threadCounter = 0;
            Thread[] ta = new Thread[maxThreadCount];

            String rssaddresses = p.getProperty("rsslisteadresi");
            System.out.println("rssaddresses = " + rssaddresses);
            Scanner s = new Scanner(new File(rssaddresses));
            System.out.println("maxThreadCount = " + maxThreadCount);
            while (s.hasNext()) {
                if (threadCounter < maxThreadCount) {
                    String line = s.nextLine();
                    //System.out.println("line = " + line);
                    //örnek: milliyet,magazin,http://www.milliyet.com.tr/rss/rssNew/magazinRss.xml
                    if (!line.trim().equals("")) {
                        System.out.println(line);
                        String[] values = line.split(",");
                        //haberlerin kayıt edileceği klasörler yoksa oluşturulur
                        makeDirs(values[0], values[1]);

                        //RSS downloader Thread
                        Thread t = new Thread(new NewsFetcherThread(values[2], values[0], values[1]));

                        //her bir RSS dosyası parse edilip haberler indirilir                
                        ta[threadCounter] = t;
                        t.start();
                        threadCounter++;
                    }
                    //System.out.println("threadCounter = " + threadCounter);
                } else if (threadCounter >= maxThreadCount) {
                    for (Thread th : ta) {
                        // System.out.println("join - " + th.getName());
                        th.join();
                    }
                    threadCounter = 0;
                }

            }
        } catch (FileNotFoundException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        NewsFetcher nf = new NewsFetcher();
        nf.fetchNewsFromRSS();
    }
}

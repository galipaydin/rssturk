package org.buyukveri.rss;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by galip on 15/04/2017.
 */
public class Downloader {

    Properties p;
    String file;

    public Downloader() {
        try {
            p = new Properties();
            p.load(new FileReader("C:\\Users\\bim\\Documents\\NetBeansProjects\\rssturk\\src/main/resources/news.properties"));
//        p = PropertyLoader.loadProperties("news");
            this.file = p.getProperty("rsslisteadresi");
            System.out.println("dir = " + file);

        } catch (Exception e) {
            System.out.println (e.getMessage () );
        }
    }

    public void fetchNews() {
        try {
            Scanner s = new Scanner(new File(this.file));
            ExecutorService executor = Executors.newFixedThreadPool(5);
            while (s.hasNext()) {
                String line = s.nextLine();
                System.out.println("line = " + line);
                String[] vals = line.split(",");
                if (vals.length == 3) {
                    String paper = vals[0];
                    String category = vals[1];
                    String url = vals[2];
                    System.out.println(paper + " - " + category);
                    Runnable worker = new RSSParser(url, paper, category,p);
//                    RSSParser worker = new RSSParser(url, paper, category);
                  //  worker.run ();
                    executor.execute(worker);
                }
            }


            executor.shutdown();
            while (!executor.isTerminated()) {
            }

            System.out.println("Finished all threads");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        Downloader t = new Downloader();
        while(true){
            t.fetchNews();
            try {
                Thread.sleep(900000);
            } catch (InterruptedException e) {
                e.printStackTrace ( );
            }
        }
    }
}

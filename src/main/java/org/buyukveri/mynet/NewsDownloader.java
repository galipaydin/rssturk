/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.mynet;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author galip
 */
public class NewsDownloader {

    public void processLinkFolder(String linkFilesFolder, String outputFolder) {
        try {
            File f = new File(linkFilesFolder);
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    readLinkFile(file, outputFolder);
                }
            }
        } catch (Exception e) {
        }
    }

    public void readLinkFile(File inputFile, String outputPath) {
        try {
            String filename = inputFile.getName();

            File f = new File(outputPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            ExecutorService executor = Executors.newFixedThreadPool(10);

            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                String url = "http://finans.mynet.com" + line;
                Runnable worker = new org.buyukveri.mynet.DownloaderThread(url, outputPath, filename);
                executor.execute(worker);
//                parseNewsPage(url, fw);
            }
            System.out.println("Finished all threads");
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        NewsDownloader n = new NewsDownloader();
//        n.parseNewsPage("http://finans.mynet.com/haber/detay/analiz/altin-analiz/122070/", null);
        n.processLinkFolder("/Users/galip/dev/data/news/mynet/links", "/Users/galip/dev/data/news/mynet/news");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.evrensel;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author galip
 */
public class DownloadNews {

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

            System.out.println("Finished all threads");
            
//            String path = outputPath + "/" + filename;
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                Runnable worker = new DownloaderThread(line, outputPath, filename);
                executor.execute(worker);
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        DownloadNews d = new DownloadNews();
        d.processLinkFolder("/store/data/news/evrensel/links",
                "/store/data/news/evrensel/news");
//        d.processLinkFolder("","");
//        d.downloadNews("https://www.evrensel.net/haber/193919/iran-da-gosteri-sirasi-hukumet-yanlilarindaydi",
//                "", "");
    }
}

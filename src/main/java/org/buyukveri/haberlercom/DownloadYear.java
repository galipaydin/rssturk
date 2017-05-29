/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.haberlercom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadYear {

    private String[] categories = {"ekonomi", "guncel", "dunya", "spor", "politika",
        "yasam", "kultur-sanat", "magazin"};

    public void downloadYear(String folderPath, int year) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(8);
            for (String category : categories) {
                Runnable worker = new NewsParser(folderPath, category, year);
                executor.execute(worker);
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
        System.out.println("Usage: NewsParser folderPath category year startMonth endMonth");
        String folderPath, year;
        if (args.length != 2) {
            System.out.println("Invalid no of parameters");
        } else {
            folderPath = args[0];
            DownloadYear d = new DownloadYear();
            year = args[1];
            d.downloadYear(folderPath, Integer.parseInt(year));
        }

//        DownloadYear d = new DownloadYear();
//        d.downloadYear("F:/dataset/news", 2006);

    }
}

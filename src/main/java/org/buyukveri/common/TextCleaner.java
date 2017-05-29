/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.common;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author galip
 */
public class TextCleaner {

    public static void cleanDigits(String text) {
        try {
            char[] chars = {'!', '\'', '#', '"', '^', '%', '&', '/', '(', ')', '=', '*', '?', '-', '_',
                ';', ',', '.', ':', '<', '>', '£', '$', '½', '§', '{', '[', ']', '}', '\\', '|', '~', '`',
                '÷', '≥', '≤', 'µ', '∫', '√', '≈', 'Ω', 'æ', 'ß', '∂', 'ƒ', '^', '∆', '¨', '¬', '´', 'æ', '~',
                'π', '¥', '₺', '®', '€', '∑', '<'};
            System.out.println(text);
            String charss = "!\\#\"^%&/()=*?-_;.,:<>£$½§{[]}\\\\|~`÷≥≤µ∫√≈Ωæß∂ƒ^∆¨¬´æ~π¥₺®€∑<";
            for (char a : text.toCharArray()) {

                if (charss.contains(String.valueOf(a))) {
                    System.out.println(a);
                    text = text.replaceAll(String.valueOf(a), " ");
                }
            }
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cleanTurkishChars(String str) {
        str = str.toLowerCase().replaceAll("ü", "u")
                .replaceAll("ğ", "g")
                .replaceAll("ş", "s")
                .replaceAll("ç", "c")
                .replaceAll("ı", "i")
                .replaceAll("ğ", "g")
                .replaceAll("ö", "o");
        return str;
    }

    public static String word2vecCleaner(String text) {
        try {
            text = text.trim();
            text = text.replaceAll("\\d.\\d", ""); //replace 15.5 with 1
            text = text.replaceAll("\\.", "\n");
            text = text.replaceAll(",", " ");
            text = text.replaceAll("\\d", " "); //replace all digitis with whitespace
            text = text.replaceAll("\\P{L}+\n", " "); //replace all non word chars
            text = text.replaceAll("[^\\n^\\p{L}\\p{Nd}]+", " "); //replace all non word chars
            text = text.replaceAll(" +", " "); //replace multiple whitespaces with only one

            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void cleanFolder(String inputFolder, String outputFolder) {
        try {
            File f = new File(inputFolder);
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    cleanFile(file, outputFolder);
                }
            }
        } catch (Exception e) {
        }
    }

    public void cleanFile(File inputFile, String outputPath) {
        try {
            String filename = inputFile.getName();
            FileWriter fw = new FileWriter(outputPath + "/" + filename);

            File f = new File(outputPath);
            if (!f.exists()) {
                f.mkdirs();
            }

            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                String text = word2vecCleaner(line);
                System.out.println(text);
                fw.write(text + "\n");
                fw.flush();
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void wordTest(File inputFile) {
        try {
            
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                Scanner s1 = new Scanner(line);
                while (s1.hasNext()) {
                    String word = s1.next();
                    if (word.length() <= 2) {
                        System.out.println(word);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String a = "!#^%&/()=*?-_;.,:<>£$½§{[]} Lenovo... { [] Windows 10 işletim sistemiyle çalışan dünyanın en ince dönüştürülebilir Intel Core i7 işlemcili."
                + "15.5 saate kadar pil ömrü sunan dizüstü bilgisayarı Yoga 910 ile 2K ekranlı."
                + "18 saate kadar pil ömrü sunan. Android Tab 3 Plus modelleri ile tüm dünyayı bir kez daha şaşırttı."
                + "Dünya’nın 1 numaralı PC. üreticisi Lenovo, Tüketici Elektroniği Fuarı IFA’da."
                + " Türkiye’de de satışa. sunulacak. müşteri geri dönüşlerine göre geliştirilmiş olan yeni PC ve tablet modellerini tanıttı."
                + "Her yeni ürününe son. teknolojiyi. katan Lenovo, ThinkPad’lerde görmeye alışık olduğumuz."
                + " parmak izi tarayıcı. gibi en yeni... teknolojik özellikleri Yoga 910’da hayata geçirdi. "
                + "14.3 mm inceliği ile dünyanın en ince... dönüştürülebilen dizüstü bilgisayarı olan 13.9 inç."
                + " ekranlı Yoga 910, aynı zamanda 7. Nesil... Intel Core i7 işlemcisi ile türünün en güçlülerinden biri."
                + "Yoga 910 şimdi daha güçlü 4K veya Full HD ekranı ve Dolby Audio™ Premium ses teknolojisi ile öne."
                + " çıkıyor. Aynı zamanda 15.5 saate kadar pil ömrü sunan bu üstün bilgisayar, kullanıcılarının şarj ."
                + "derdine son veriyor."
                + "Şampanya, altın ve gri metalik renk seçenekleri bulunan Yoga 910, lüks ve bir o kadar da şık bir tasarıma sahip. "
                + "Meşhur saat kayışı modeli Yoga menteşe tasarımına sahip bu cihaz, diğer Yoga’larda olduğu gibi 4 farklı modda kullanılabiliyor. "
                + "Dizüstü, stant, çadır ve tablet modu.";
        String test = "!#^%&/()=*?-_;.,:<>£$½§{[]}";

//        test = test.replaceAll("\\W", ""); 
//        TextCleaner.word2vecCleaner(a);
        TextCleaner t = new TextCleaner();
//        t.cleanFolder("F:/data/news", "F:/data/clean");
        t.wordTest(new File("F:/data/clean/2009_guncel.txt"));
    }
}

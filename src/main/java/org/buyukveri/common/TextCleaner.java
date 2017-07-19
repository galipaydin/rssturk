/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.common;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            text = text.replaceAll("\n", " . \n");
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String fastTextCleaner(String text) {
        try {
            String label = "";

            if (text.startsWith("__label__pozitif")) {
                label = "__label__pozitif";
                text = text.substring(17);
            }
            else if (text.startsWith("__label__negatif")) {
                    label = "__label__pozitif";
                    text = text.substring(17);

                    text = text.trim();
                    text = text.replaceAll("\\d.\\d", ""); //replace 15.5 with 1
//            text = text.replaceAll("\\.", "\n");
                    text = text.replaceAll(",", " ");
                    text = text.replaceAll("\\d", " "); //replace all digitis with whitespace
                    text = text.replaceAll("\\P{L}+\n", " "); //replace all non word chars
                    text = text.replaceAll("[^\\n^\\p{L}\\p{Nd}]+", " "); //replace all non word chars
                    text = text.replaceAll(" +", " "); //replace multiple whitespaces with only one
//            text = text.replaceAll("\n", " . \n");
                text = label + " " + text;
                }
            
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void cleanFolder(String inputFolder, String outputFolder) {
        try {
            File f1 = new File(outputFolder);
            if (!f1.exists()) {
                f1.mkdirs();
            }

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
                text = cleanTurkishChars(text);
                text = repeatingChars(text);
//                System.out.println(text);
                fw.write(text + "\n");
                fw.flush();
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fasTextCleanFile(File inputFile, String outputPath) {
        try {
            String filename = inputFile.getName();
            FileWriter fw = new FileWriter(outputPath + "/" + filename);

            File f = new File(outputPath);
            if (!f.exists()) {
                System.out.println("***");
                f.mkdirs();
            }

            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine();
                String text = fastTextCleaner(line);
//                System.out.println(text);
                fw.write(text + "\n");
                fw.flush();
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void wordCountFiles(File inputFile, String outputFolder) {
        try {
            File f = new File(outputFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine().toLowerCase();
                String text = word2vecCleaner(line);

                Scanner s1 = new Scanner(text);

                while (s1.hasNext()) {
                    String word = s1.next();
                    word = repeatingChars(word);

                    int length = word.length();
                    FileWriter fw = new FileWriter(outputFolder + "/" + length + "_harfliler.txt", true);
                    fw.write(word + "\n");
                    fw.flush();
                    fw.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String repeatingChars(String line) {
        String ret = "";
        Scanner s = new Scanner(line);

        while (s.hasNext()) {
            String word = s.next();
            String regex = "([a-z\\d])\\1\\1";
            String regex1 = ".*([a-z])\\1{2,}.*";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(word);

            if (m.find()) {
//                System.out.println("Duplicate character " + m.group(1) + "-->" + word);
//            word = word.replaceAll(m.group(1)+"{2,}", m.group(1));
                word = word.replaceAll(m.group(1) + "+", m.group(1));
                word = repeatingChars(word);
                ret += word.trim();
            } else {
                ret += word.trim();
//            return word;
            }
            ret += " ";
        }
        return ret.trim();
    }

    public void multipleWords(String inputFilePath, String outputFolder) {
        try {
            FileWriter cokfw = new FileWriter(outputFolder + "/" + "cok_kelime.txt");
            FileWriter tekfw = new FileWriter(outputFolder + "/" + "tek_kelime.txt");

            File inputFile = new File(inputFilePath);
            File f = new File(outputFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
            Scanner s = new Scanner(inputFile);
            while (s.hasNext()) {
                String line = s.nextLine().toLowerCase();
                String[] tokens = line.split(" ");
                if (tokens.length > 1) {
                    cokfw.write(line + "\n");
                    cokfw.flush();
                } else {
                    tekfw.write(line + "\n");
                    tekfw.flush();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uniqueWords(String infile, String outfile) {
        try {
            FileWriter fw = new FileWriter(outfile);
            Scanner s = new Scanner(new File(infile));
            s.useDelimiter("\\Z");
            String content = s.next();
//            System.out.println(content);

            String[] words = content.split("\n");
            Set<String> uniqueWords = new HashSet<String>();

            for (String word : words) {
                uniqueWords.add(word);
            }

            TreeSet sortedset = new TreeSet();
            sortedset.addAll(uniqueWords);

            List list = new ArrayList();
            list.addAll(sortedset);

//            uniqueWords.stream().sorted().collect(Collectors.toList());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                fw.write((String) it.next() + "\n");
                fw.flush();
            }

        } catch (Exception e) {
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
//        t.cleanFile(new File("/Users/galip/NetBeansProjects/dl4j-examples/dl4j-examples/src/main/resources/dunya/dunya.txt"),
//                "/Users/galip/NetBeansProjects/dl4j-examples/dl4j-examples/src/main/resources/dunya/clean");
        
t.cleanFolder("/Users/galip/NetBeansProjects/dl4j-examples/dl4j-examples/src/main/resources/author2vec/unlabeled", 
        "/Users/galip/NetBeansProjects/dl4j-examples/dl4j-examples/src/main/resources/author2vec/unlabeled/clean");
//      String b =   t.word2vecCleaner(a);
//        System.out.println(b);
//        t.fasTextCleanFile(new File("/Users/galip/dev/data/beyazperde/fasttext/uclabel.test"), "/Users/galip/dev/data/beyazperde/fasttext/clean");

//        t.cleanFolder("/Users/galip/dev/data/beyazperde/comments", "/Users/galip/dev/data/beyazperde/comments/clean");
//        t.wordCountFiles(new File("/Users/galip/dev/data/sozluk/sozluk/tek_unique.txt"),
//                "/Users/galip/dev/data/sozluk/sozluk/");
//        String s = t.repeatingChars("ssuuuuuuppppppppeeeeeerrrrrrccddeeff ddduuuuupppppeeeerrrr");
//        System.out.println("s = " + s);
//        t.multipleWords("/Users/galip/dev/data/sozluk/sozluk/kelime-listesi.txt", "/Users/galip/dev/data/sozluk/sozluk");
//        t.uniqueWords("/Users/galip/dev/data/sozluk/sozluk/tek_kelime_hepsi.txt",
//                "/Users/galip/dev/data/sozluk/sozluk/tek_unique.txt");
    }
}

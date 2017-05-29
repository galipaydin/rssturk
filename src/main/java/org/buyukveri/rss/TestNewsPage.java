/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.rss;

import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.kohlschutter.boilerpipe.extractors.LargestContentExtractor;
import java.nio.charset.Charset;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;

/**
 *
 * @author bim
 */
public class TestNewsPage {

    public void testPage(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            String html = doc.html();
            ArticleExtractor a = ArticleExtractor.INSTANCE;
            String content = LargestContentExtractor.INSTANCE.getText(html);
            Charset.forName("UTF-8").encode(content);
            System.out.println("lce = \n" + content);
            String test = a.getText(html);
            System.out.println("ae = \n" + test);

        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
            TestNewsPage t = new TestNewsPage();
            t.testPage("http://www.haberler.com/samsun-da-polise-silahli-saldiri-haberi/");
    }
}

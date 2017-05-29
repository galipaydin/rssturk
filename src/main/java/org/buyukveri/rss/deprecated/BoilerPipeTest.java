/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.rss.deprecated;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author galip
 */
public class BoilerPipeTest {
    public static void main(String[] args) {
        
                    try {
                        
                        final URL linkurl = new URL("http://secure.milliyet.com.tr/redirect/Default.aspx?l=http%3a%2f%2fwww.milliyet.com.tr%2f-ne-ara-vicdanimizi-kaybettik--magazin-2382156%2f%3futm_source%3drss%26amp%3butm_medium%3dmilliyetmagazinrss\n" );
                        String content = "";
                        //LargestContentExtractor.INSTANCE.getText(linkurl);
                        ArticleExtractor a = ArticleExtractor.INSTANCE;
                        
                        try {
                            if (a.getText(linkurl) != null) {
                                content = a.getText(linkurl);
                                System.out.println("content = " + content);
                            }
                        } catch (BoilerpipeProcessingException ex) {
                            System.out.println("linkurl = " + linkurl);
                            System.out.println("parseRSS-BoilerpipeProcessingException: " + ex.getMessage());
                        }
                        
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(BoilerPipeTest.class.getName()).log(Level.SEVERE, null, ex);
                    }

    }
}

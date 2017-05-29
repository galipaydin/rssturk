package org.buyukveri.rss.deprecated;

import com.sun.syndication.io.SAXBuilder;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.impl.XmlFixerReader;
import java.io.Reader;
import org.jdom.Document;

public class MyWireFeedInput extends WireFeedInput {

  public  Document getDocument(Reader reader) {
        final SAXBuilder saxBuilder = createSAXBuilder();
        try {
            return saxBuilder.build(new XmlFixerReader(reader));
        } catch ( Exception ex) {
            return null;
        }
    }
}
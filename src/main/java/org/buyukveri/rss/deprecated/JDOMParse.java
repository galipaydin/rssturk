
package org.buyukveri.rss.deprecated;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author galip
 */
public class JDOMParse {

    public static void JdomWalkTree(Element e) {
        List<Element> es = e.getChildren();
        if (es.size() > 0) {
            for (Element el : es) {
                JdomWalkTree(el);
            }
        } else {
            System.out.println(e.toString());
            System.out.println(e.getName() + " : " + e.getValue());
            if (e.getAttributes().size() > 0) {
                for (Object a : e.getAttributes()) {
                    Attribute at = (Attribute) a;
                    //System.out.println("\t" + at.getName() + " : " + at.getValue());
                }
            }
        }

    }

    public static String getAttributeValue(Element e, String atName) {
        if (e.getAttributes().size() > 0) {
            for (Object a : e.getAttributes()) {
                Attribute at = (Attribute) a;
                if (at.getName().equals(atName)) {
                    //System.out.println("\t" + at.getName() + " : " + at.getValue());
                    return at.getValue();
                }
            }
        }
        return "";
    }

    public static String getImageURL(Element e, String gazete) {
        for (Object o : e.getChildren()) {
            Element el = (Element) o;
            String elName = el.getName();
            gazete = gazete.toLowerCase();

            if (gazete.equals("sabah") || gazete.equals("cumhuriyet") || gazete.equals("star")) {
                if (elName.trim().toLowerCase().equals("content")) {
                    return getAttributeValue(el, "url");
                }
            } else if (gazete.equals("hurriyet") || gazete.equals("turkiye")) {
                if (elName.trim().toLowerCase().equals("thumbnail")) {
//                    System.out.println(el.getAttributeValue("url"));
                    return el.getAttributeValue("url");
                }

            } else if (gazete.equals("milliyet")) {
                if (elName.trim().toLowerCase().equals("imageurl")) {
                    return el.getValue();
                }
            } else if (gazete.equals("aa") || gazete.equals("milat")) {
                if (elName.trim().toLowerCase().equals("image")) {
                    return el.getValue();
                }
            } else if (gazete.equals("yenisafak")) {
                System.out.println(el.getValue());
                if (elName.trim().toLowerCase().equals("img")) {
                    System.out.println(el.getValue());
                    return el.getValue();
                }
            }
        }
        return "";
    }
}

package tibia;

import jsoup.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TibiaParserUtil {

    public static Element getElementForAssociatedData(Document document, String key) {
        try {
            return JsoupUtil.elementsWhichEqual(document.getElementsByTag("td"), key).first().nextElementSibling();
        }catch (NullPointerException e) {
            System.out.println(document.getElementsByTag("td"));
            return null;
        }
    }

}

package tibia;

import jsoup.JsoupUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TibiaParserUtil {

    public static Element getElementForAssociatedData(Document document, String key) {
        return JsoupUtil.elementsWhichEqual(document.getElementsByTag("td"), key).first().nextElementSibling();
    }

}

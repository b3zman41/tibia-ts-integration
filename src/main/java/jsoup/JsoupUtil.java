package jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class JsoupUtil {

    public static Elements elementsWhichContain(Elements elements, String needle) {
        ArrayList list = new ArrayList();

        for(Element element : elements) {
            if (element.text().contains(needle)) {
                list.add(element);
            }
        }

        return new Elements(list);
    }

    public static Elements elementsWhichEqual(Elements elements, String needle) {
        ArrayList list = new ArrayList();

        for(Element element : elements) {
            if (element.text().equals(needle)) {
                list.add(element);
            }
        }

        return new Elements(list);
    }

}

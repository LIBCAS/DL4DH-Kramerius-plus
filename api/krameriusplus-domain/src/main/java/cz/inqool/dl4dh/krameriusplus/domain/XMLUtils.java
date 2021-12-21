package cz.inqool.dl4dh.krameriusplus.domain;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class XMLUtils {

    public static <T> T getFirstIfOnly(List<T> elements) {
        if (elements.size() != 1) {
            throw new IllegalArgumentException("Expected list to contain one element, got " + elements.size());
        }

        return elements.get(0);
    }

    public static Node getFirstIfOnly(NodeList elements) {
        if (elements.getLength() != 1) {
            throw new IllegalArgumentException("Expected list to contain one element, got " + elements.getLength());
        }

        return elements.item(0);
    }
}

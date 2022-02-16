package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsElement;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.DomParser;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO - Helper class, clients should create a new instance for every usage
 * @param <T>
 * @param <E>
 */
public abstract class NDKExtractor<T extends MetsElement, E> {

    protected final DomParser domParser;

    private final String ATTRIBUTE_ID_PREFIX;

    private final String PREMIS_TAG_NAME;

    private final Class<E> clazz;

    private final Map<String, T> result = new HashMap<>();

    public NDKExtractor(DomParser domParser, String attributeIdPrefix, String premisTagName, Class<E> clazz) {
        this.domParser = domParser;
        this.ATTRIBUTE_ID_PREFIX = attributeIdPrefix;
        this.PREMIS_TAG_NAME = premisTagName;
        this.clazz = clazz;
    }

    public Map<String, T> extract(NodeList nodeList) {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                extractXmlComplexType(element);
            }
        }

        return result;
    }


    private void extractXmlComplexType(Element element) {
        String elementId = element.getAttribute("ID");

        if (elementId.startsWith(ATTRIBUTE_ID_PREFIX)) {
            String nodeAsString = domParser.nodeToString(XMLUtils.getFirstIfOnly(element.getElementsByTagName(PREMIS_TAG_NAME)));

            E xmlComplexType = JAXB.unmarshal(new StringReader(nodeAsString), clazz);

            if (xmlComplexType != null) {
                T metsPremis = processXmlComplexType(xmlComplexType);
                metsPremis.setId(elementId);

                result.put(elementId, metsPremis);
            }
        }
    }

    protected abstract T processXmlComplexType(E xmlComplexType);
}

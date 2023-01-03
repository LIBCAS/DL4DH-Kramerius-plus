package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

@Component
public class DomParser {

    private final DocumentBuilder documentBuilder;

    private final Transformer transformer;

    public DomParser() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            throw new IllegalStateException("Failed to create documentBuilderFactory", e);
        }
    }

    public Document parse(File file) {
        try {
            return documentBuilder.parse(file);
        } catch (SAXException | IOException e) {
            throw new IllegalStateException("Failed to parse XML file");
        }
    }

    public String nodeToString(Node node) {
        StringWriter stringWriter = new StringWriter();
        try {
            transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
        } catch (TransformerException te) {
            throw new IllegalStateException("Error transforming xml node '<" + node.getNodeName() + ">' to String");
        }

        return stringWriter.toString();
    }
}

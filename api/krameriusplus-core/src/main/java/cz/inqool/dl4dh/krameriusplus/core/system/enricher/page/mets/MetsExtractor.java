package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.DomParser;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors.MixExtractor;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors.PremisAgentExtractor;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors.PremisEventExtractor;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors.PremisObjectExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.nio.file.Path;

@Service
public class MetsExtractor {

    private final DomParser domParser;

    @Autowired
    public MetsExtractor(DomParser domParser) {
        this.domParser = domParser;
    }

    public void enrich(Page page) {
        Path metsPath = page.getMetsPath();

        if (metsPath == null) {
            return;
        }

        Document document = domParser.parse(metsPath.toFile());
        document.getDocumentElement().normalize();

        MetsMetadata metsMetadata = new MetsMetadata();

        NodeList techMdNodes = extractNodeList(document, "mets:techMD");
        NodeList digiprovMDNodes = extractNodeList(document, "mets:digiprovMD");

        metsMetadata.setPremisObjects(new PremisObjectExtractor(domParser).extract(techMdNodes));
        metsMetadata.setPremisEvents(new PremisEventExtractor(domParser).extract(digiprovMDNodes));
        metsMetadata.setPremisAgents(new PremisAgentExtractor(domParser).extract(digiprovMDNodes));
        metsMetadata.setMix(new MixExtractor(domParser).extract(techMdNodes));

        page.setMetsMetadata(metsMetadata);
    }

    private NodeList extractNodeList(Document document, String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);

        if (nodeList.getLength() < 1) {
            throw new IllegalStateException("No nodes with tag <" + tagName + "> were found");
        }

        return nodeList;
    }
}

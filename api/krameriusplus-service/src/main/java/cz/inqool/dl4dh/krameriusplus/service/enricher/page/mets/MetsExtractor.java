package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.service.enricher.DomParser;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors.MixExtractor;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors.PremisAgentExtractor;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors.PremisEventExtractor;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors.PremisObjectExtractor;
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

    public void enrich(Path pageMets, Page page) {
        Document document = domParser.parse(pageMets.toFile());
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

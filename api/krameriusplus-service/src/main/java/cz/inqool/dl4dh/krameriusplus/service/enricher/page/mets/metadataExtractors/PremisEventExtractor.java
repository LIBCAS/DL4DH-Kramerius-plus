package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.domain.util.XMLUtils;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.DomParser;
import info.lc.xmlns.premis_v2.EventComplexType;

public class PremisEventExtractor extends NDKExtractor<MetsPremisEventElement, EventComplexType> {

    public PremisEventExtractor(DomParser domParser) {
        super(domParser, "EVT", "premis:event", EventComplexType.class);
    }

    @Override
    protected MetsPremisEventElement processXmlComplexType(EventComplexType event) {
        MetsPremisEventElement metsPremisEventElement = new MetsPremisEventElement();

        metsPremisEventElement.setId(event.getXmlID());
        metsPremisEventElement.setEventType(event.getEventType());
        metsPremisEventElement.setEventDateTime(event.getEventDateTime());
        metsPremisEventElement.setEventDetail(event.getEventDetail());
        metsPremisEventElement.setEventOutcomeInformation(new MetsPremisEventElement.EventOutcomeInformation(
                XMLUtils.getFirstIfOnly(event.getEventOutcomeInformation())));
        metsPremisEventElement.setLinkingAgentIdentifier(new MetsPremisEventElement.LinkingAgentIdentifier(
                XMLUtils.getFirstIfOnly(event.getLinkingAgentIdentifier())));

        return metsPremisEventElement;
    }
}

package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
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

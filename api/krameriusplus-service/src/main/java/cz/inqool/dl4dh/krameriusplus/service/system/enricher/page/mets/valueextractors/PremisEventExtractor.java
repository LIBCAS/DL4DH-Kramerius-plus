package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.mets.MdSecType;
import info.lc.xmlns.premis_v2.EventComplexType;
import info.lc.xmlns.premis_v2.PremisComplexType;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

public class PremisEventExtractor extends NDKExtractor<MetsPremisEventElement, EventComplexType> {

    public PremisEventExtractor(DomParser domParser) {
        super(domParser, "EVT");
    }

    @Override
    protected MetsPremisEventElement processMetadataSection(MdSecType mdSection) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(mdSection.getMdWrap().getXmlData(), stringWriter);
        PremisComplexType premisComplexType = JAXB.unmarshal(new StringReader(stringWriter.toString()), PremisComplexType.class);

        EventComplexType event = premisComplexType.getEvent().get(0);
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

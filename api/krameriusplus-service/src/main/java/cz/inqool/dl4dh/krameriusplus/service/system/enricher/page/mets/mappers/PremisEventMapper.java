package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import info.lc.xmlns.premis_v2.EventComplexType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class PremisEventMapper implements MetsMapper<MetsPremisEventElement> {

    @Override
    public MetsPremisEventElement map(String source) {
        EventComplexType extracted = JAXB.unmarshal(new StringReader(source), EventComplexType.class);
        MetsPremisEventElement metsPremisEventElement = new MetsPremisEventElement();

        metsPremisEventElement.setId(extracted.getXmlID());
        metsPremisEventElement.setEventType(extracted.getEventType());
        metsPremisEventElement.setEventDateTime(extracted.getEventDateTime());
        metsPremisEventElement.setEventDetail(extracted.getEventDetail());
        metsPremisEventElement.setEventOutcomeInformation(new MetsPremisEventElement.EventOutcomeInformation(
                XMLUtils.getFirstIfOnly(extracted.getEventOutcomeInformation())));
        metsPremisEventElement.setLinkingAgentIdentifier(new MetsPremisEventElement.LinkingAgentIdentifier(
                XMLUtils.getFirstIfOnly(extracted.getLinkingAgentIdentifier())));

        return metsPremisEventElement;
    }

    @Override
    public Class<MetsPremisEventElement> supports() {
        return MetsPremisEventElement.class;
    }
}

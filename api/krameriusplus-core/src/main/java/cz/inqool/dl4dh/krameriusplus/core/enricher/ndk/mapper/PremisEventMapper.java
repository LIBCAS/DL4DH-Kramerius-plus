package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.mapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisEventElement.EventOutcomeInformation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisEventElement.EventOutcomeInformation.EventOutcomeDetail;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisEventElement.LinkingAgentIdentifier;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import cz.inqool.dl4dh.ndk.premis.EventComplexType;
import cz.inqool.dl4dh.ndk.premis.EventOutcomeDetailComplexType;
import cz.inqool.dl4dh.ndk.premis.EventOutcomeInformationComplexType;
import cz.inqool.dl4dh.ndk.premis.LinkingAgentIdentifierComplexType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.xml.bind.JAXBElement;
import java.util.List;

@Mapper
public interface PremisEventMapper extends MetsMapperBase {

    MetsPremisEventElement convert(EventComplexType xmlElement);

    @Mappings({
            @Mapping(target = "identifierType", source = "linkingAgentIdentifierType"),
            @Mapping(target = "identifierValue", source = "linkingAgentIdentifierValue"),
            @Mapping(target = "roles", source = "linkingAgentRole"),
    })
    LinkingAgentIdentifier map(LinkingAgentIdentifierComplexType xmlElement);

    default EventOutcomeInformation mapEventOutcome(List<EventOutcomeInformationComplexType> xmlElement) {
        List<JAXBElement<?>> content = XMLUtils.getFirstIfOnly(xmlElement).getContent();
        EventOutcomeInformation eventOutcomeInformation = new EventOutcomeInformation();

        for (JAXBElement<?> child : content) {
            if (child.getValue() instanceof String && child.getName().getLocalPart().equals("eventOutcome")) {
                eventOutcomeInformation.setOutcome(child.getValue().toString());
            } if (child.getValue() instanceof EventOutcomeDetailComplexType) {
                eventOutcomeInformation.setDetail(map((EventOutcomeDetailComplexType) child.getValue()));
            }
        }

        return eventOutcomeInformation;
    }

    default LinkingAgentIdentifier mapLinkingAgent(List<LinkingAgentIdentifierComplexType> xmlElement) {
        return map(XMLUtils.getFirstIfOnly(xmlElement));
    }

    default EventOutcomeDetail map(EventOutcomeDetailComplexType xmlElement) {
        EventOutcomeDetail eventOutcomeDetail = new EventOutcomeDetail();

        List<JAXBElement<?>> content = xmlElement.getContent();

        for (JAXBElement<?> child : content) {
            String tagName = child.getName().getLocalPart();

            switch (tagName) {
                case "eventOutcomeDetailNote":
                    eventOutcomeDetail.setNote(child.getValue().toString());
                    break;
                case "eventOutcomeDetailExtension":
                    eventOutcomeDetail.setExtension(child.getValue().toString());
                    break;
            }
        }

        return eventOutcomeDetail;
    }
}

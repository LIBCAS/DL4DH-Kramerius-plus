package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.event;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.MetsElement;
import info.lc.xmlns.premis_v2.EventOutcomeDetailComplexType;
import info.lc.xmlns.premis_v2.EventOutcomeInformationComplexType;
import info.lc.xmlns.premis_v2.LinkingAgentIdentifierComplexType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.JAXBElement;
import java.util.List;

@Getter
@Setter
public class MetsPremisEventElement extends MetsElement {

    private String eventType;

    private String eventDateTime;

    private String eventDetail;

    private EventOutcomeInformation eventOutcomeInformation;

    private LinkingAgentIdentifier linkingAgentIdentifier;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class EventOutcomeInformation {

        private String outcome;
        private EventOutcomeDetail detail;

        public EventOutcomeInformation(EventOutcomeInformationComplexType xmlElement) {
            List<JAXBElement<?>> content = xmlElement.getContent();

            for (JAXBElement<?> child : content) {
                if (child.getValue() instanceof String && child.getName().getLocalPart().equals("eventOutcome")) {
                    this.outcome = child.getValue().toString();
                } if (child.getValue() instanceof EventOutcomeDetailComplexType) {
                    this.detail = new EventOutcomeDetail((EventOutcomeDetailComplexType) child.getValue());
                }
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class EventOutcomeDetail {
            private String note;
            private String extension;

            public EventOutcomeDetail(EventOutcomeDetailComplexType xmlElement) {
                List<JAXBElement<?>> content = xmlElement.getContent();

                for (JAXBElement<?> child : content) {
                    String tagName = child.getName().getLocalPart();

                    switch (tagName) {
                        case "eventOutcomeDetailNote":
                            this.note = child.getValue().toString();
                            break;
                        case "eventOutcomeDetailExtension":
                            this.extension = child.getValue().toString();
                            break;
                    }
                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LinkingAgentIdentifier {
        private String identifierType;
        private String identifierValue;
        private List<String> roles;

        public LinkingAgentIdentifier(LinkingAgentIdentifierComplexType xmlElement) {
            this.identifierType = xmlElement.getLinkingAgentIdentifierType();
            this.identifierValue= xmlElement.getLinkingAgentIdentifierValue();
            this.roles = xmlElement.getLinkingAgentRole();
        }
    }
}

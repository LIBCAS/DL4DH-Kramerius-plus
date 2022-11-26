package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

        @Getter
        @Setter
        @NoArgsConstructor
        public static class EventOutcomeDetail {
            private String note;
            private String extension;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LinkingAgentIdentifier {
        private String identifierType;
        private String identifierValue;
        private List<String> roles;
    }
}

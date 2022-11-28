package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetsPremisAgentElement extends MetsElement {

    private List<AgentIdentifier> agentIdentifiers;

    private List<String> agentNames;

    private String agentType;

    private String agentNote;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AgentIdentifier {
        private String identifierType;
        private String identifierValue;
    }
}

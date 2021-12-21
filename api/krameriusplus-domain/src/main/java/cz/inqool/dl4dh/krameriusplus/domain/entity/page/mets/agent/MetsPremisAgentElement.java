package cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.agent;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.MetsElement;
import info.lc.xmlns.premis_v2.AgentIdentifierComplexType;
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

        public AgentIdentifier(AgentIdentifierComplexType xmlElement) {
            this.identifierType = xmlElement.getAgentIdentifierType();
            this.identifierValue = xmlElement.getAgentIdentifierValue();
        }
    }
}

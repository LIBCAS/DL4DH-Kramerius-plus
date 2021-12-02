package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.agent.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.service.enricher.DomParser;
import info.lc.xmlns.premis_v2.AgentComplexType;

import java.util.stream.Collectors;

public class PremisAgentExtractor extends NDKExtractor<MetsPremisAgentElement, AgentComplexType> {

    public PremisAgentExtractor(DomParser domParser) {
        super(domParser, "AGENT", "premis:agent", AgentComplexType.class);
    }

    @Override
    protected MetsPremisAgentElement processXmlComplexType(AgentComplexType xmlComplexType) {
        MetsPremisAgentElement agent = new MetsPremisAgentElement();
        agent.setAgentIdentifiers(xmlComplexType.getAgentIdentifier()
                .stream()
                .map(MetsPremisAgentElement.AgentIdentifier::new)
                .collect(Collectors.toList()));
        agent.setAgentNames(xmlComplexType.getAgentName());
        agent.setAgentType(xmlComplexType.getAgentType());
//        agent.setAgentNote(xmlComplexType.getAgentNote());

        return agent;
    }
}

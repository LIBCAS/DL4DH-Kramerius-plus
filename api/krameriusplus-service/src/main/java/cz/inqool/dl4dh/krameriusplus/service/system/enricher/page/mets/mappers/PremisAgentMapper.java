package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.agent.MetsPremisAgentElement;
import info.lc.xmlns.premis_v2.AgentComplexType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.stream.Collectors;

@Component
public class PremisAgentMapper implements MetsMapper<MetsPremisAgentElement> {

    @Override
    public MetsPremisAgentElement map(String source) {
        AgentComplexType agentComplexType = JAXB.unmarshal(new StringReader(source), AgentComplexType.class);

        MetsPremisAgentElement agent = new MetsPremisAgentElement();
        agent.setAgentIdentifiers(agentComplexType.getAgentIdentifier()
                .stream()
                .map(MetsPremisAgentElement.AgentIdentifier::new)
                .collect(Collectors.toList()));
        agent.setAgentNames(agentComplexType.getAgentName());
        agent.setAgentType(agentComplexType.getAgentType());
//        agent.setAgentNote(agentComplexType.getAgentNote());

        return agent;
    }

    @Override
    public Class<MetsPremisAgentElement> supports() {
        return MetsPremisAgentElement.class;
    }
}

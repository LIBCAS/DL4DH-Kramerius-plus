package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper.PremisAgentMapper;
import cz.inqool.dl4dh.ndk.premis.AgentComplexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class PremisAgentConverter implements MetsConverter<MetsPremisAgentElement> {

    private PremisAgentMapper mapper;

    @Override
    public MetsPremisAgentElement convert(String source, String elementId) {
        AgentComplexType agentComplexType = JAXB.unmarshal(new StringReader(source), AgentComplexType.class);

        MetsPremisAgentElement agent = mapper.convert(agentComplexType);
        agent.setId(elementId);

        return agent;
    }

    @Override
    public Class<MetsPremisAgentElement> supports() {
        return MetsPremisAgentElement.class;
    }

    @Autowired
    public void setMapper(PremisAgentMapper mapper) {
        this.mapper = mapper;
    }
}

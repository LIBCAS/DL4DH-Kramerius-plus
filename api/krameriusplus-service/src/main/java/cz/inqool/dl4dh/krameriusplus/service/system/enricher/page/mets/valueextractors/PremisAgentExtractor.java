package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.agent.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.mets.MdSecType;
import info.lc.xmlns.premis_v2.AgentComplexType;
import info.lc.xmlns.premis_v2.PremisComplexType;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

public class PremisAgentExtractor extends NDKExtractor<MetsPremisAgentElement, AgentComplexType> {

    public PremisAgentExtractor(DomParser domParser) {
        super(domParser, "AGENT");
    }

    @Override
    protected MetsPremisAgentElement processMetadataSection(MdSecType mdSection) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(mdSection.getMdWrap().getXmlData(), stringWriter);
        PremisComplexType premisComplexType = JAXB.unmarshal(new StringReader(stringWriter.toString()), PremisComplexType.class);
        AgentComplexType agentElement = premisComplexType.getAgent().get(0);

        MetsPremisAgentElement agent = new MetsPremisAgentElement();
        agent.setAgentIdentifiers(agentElement.getAgentIdentifier()
                .stream()
                .map(MetsPremisAgentElement.AgentIdentifier::new)
                .collect(Collectors.toList()));
        agent.setAgentNames(agentElement.getAgentName());
        agent.setAgentType(agentElement.getAgentType());
//        agent.setAgentNote(xmlComplexType.getAgentNote());

        return agent;
    }
}

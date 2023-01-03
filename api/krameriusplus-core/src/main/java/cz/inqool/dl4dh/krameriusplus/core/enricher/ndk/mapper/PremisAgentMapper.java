package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.mapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisAgentElement.AgentIdentifier;
import cz.inqool.dl4dh.ndk.premis.AgentComplexType;
import cz.inqool.dl4dh.ndk.premis.AgentIdentifierComplexType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PremisAgentMapper extends MetsMapperBase {

    @Mappings({
            @Mapping(target = "agentIdentifiers", source = "agentIdentifier"),
            @Mapping(target = "agentNames", source = "agentName")
    })
    MetsPremisAgentElement convert(AgentComplexType xmlElement);

    @Mappings({
            @Mapping(target = "identifierType", source = "agentIdentifierType"),
            @Mapping(target = "identifierValue", source = "agentIdentifierValue")
    })
    AgentIdentifier map(AgentIdentifierComplexType xmlElement);


}

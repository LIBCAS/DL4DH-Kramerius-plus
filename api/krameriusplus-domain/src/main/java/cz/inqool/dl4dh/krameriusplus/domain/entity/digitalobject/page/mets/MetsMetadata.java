package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.agent.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.mix.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.MetsPremisObjectElement;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MetsMetadata {

    private Map<String, MetsPremisObjectElement> premisObjects = new HashMap<>();

    private Map<String, MetsPremisEventElement> premisEvents = new HashMap<>();

    private Map<String, MetsPremisAgentElement> premisAgents = new HashMap<>();

    private Map<String, MetsMixElement> mix = new HashMap<>();
}

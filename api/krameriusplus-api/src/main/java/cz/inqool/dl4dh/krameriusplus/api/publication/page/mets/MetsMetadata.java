package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

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

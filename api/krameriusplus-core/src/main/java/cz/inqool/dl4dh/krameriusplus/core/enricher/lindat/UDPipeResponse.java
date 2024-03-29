package cz.inqool.dl4dh.krameriusplus.core.enricher.lindat;

import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.UDPipeEnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UDPipeResponse {

    private List<Token> tokens = new ArrayList<>();

    private UDPipeEnrichmentParadata paradata;
}

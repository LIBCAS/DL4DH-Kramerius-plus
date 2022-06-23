package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.UDPipeEnrichmentParadata;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UDPipeProcessDto {

    private List<Token> tokens = new ArrayList<>();

    private UDPipeEnrichmentParadata paradata;
}

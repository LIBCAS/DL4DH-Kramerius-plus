package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.nametag.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.NameTagEnrichmentParadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameTagProcessDto {

    private NameTagMetadata metadata;

    private NameTagEnrichmentParadata paradata;
}

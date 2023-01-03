package cz.inqool.dl4dh.krameriusplus.corev2.enricher.lindat;

import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.NameTagEnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.NameTagMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameTagResponse {

    private NameTagMetadata metadata;

    private NameTagEnrichmentParadata paradata;
}

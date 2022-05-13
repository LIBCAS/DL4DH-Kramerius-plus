package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.EnrichNdkJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichNdkRequestDto implements EnrichmentRequestDto {

    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();

    private EnrichNdkJobConfigDto config;
}

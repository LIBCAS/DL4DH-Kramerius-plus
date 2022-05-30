package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.EnrichmentNdkJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichmentNdkRequestDto implements EnrichmentRequestDto {

    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();

    @NotNull
    private EnrichmentNdkJobConfigDto config;
}

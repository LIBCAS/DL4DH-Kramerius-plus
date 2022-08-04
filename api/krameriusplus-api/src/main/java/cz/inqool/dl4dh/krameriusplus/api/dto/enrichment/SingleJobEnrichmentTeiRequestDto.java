package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment.EnrichmentTeiJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SingleJobEnrichmentTeiRequestDto extends SingleJobEnrichmentRequestBase {

    @NotNull
    private EnrichmentTeiJobConfigDto config;
}

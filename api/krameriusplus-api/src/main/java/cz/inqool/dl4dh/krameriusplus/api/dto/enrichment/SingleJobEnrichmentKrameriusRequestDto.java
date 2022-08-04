package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment.EnrichmentKrameriusJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SingleJobEnrichmentKrameriusRequestDto extends SingleJobEnrichmentRequestBase {

    @NotNull
    private EnrichmentKrameriusJobConfigDto config;
}

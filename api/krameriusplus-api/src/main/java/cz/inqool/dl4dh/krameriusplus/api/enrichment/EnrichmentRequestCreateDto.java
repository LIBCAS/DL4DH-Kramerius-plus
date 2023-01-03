package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentRequestCreateDto extends RequestCreateDto {

    private List<EnrichmentJobConfigDto> configs = new ArrayList<>();
}

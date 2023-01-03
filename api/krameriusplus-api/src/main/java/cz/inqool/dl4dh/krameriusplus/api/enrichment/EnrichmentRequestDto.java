package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.RequestDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentRequestDto extends RequestDto {

    private List<EnrichmentJobConfigDto> configs = new ArrayList<>();

    private List<EnrichmentRequestItemDto> items = new ArrayList<>();

    private KrameriusJobInstanceDto createRequestJob;
}

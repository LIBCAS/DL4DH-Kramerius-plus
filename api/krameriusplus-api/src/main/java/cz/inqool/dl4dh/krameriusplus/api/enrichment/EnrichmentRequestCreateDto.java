package cz.inqool.dl4dh.krameriusplus.api.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentJobConfigDto;

import java.util.ArrayList;
import java.util.List;

public class EnrichmentRequestCreateDto extends RequestCreateDto {

    private List<EnrichmentJobConfigDto> configs = new ArrayList<>();
}

package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;

import java.util.Set;

public interface EnrichmentRequestDto {

    /**
     * Get config, that should be used on every publication
     *
     */
    JobEventConfigCreateDto getConfig();

    /**
     * Get set of publication ids, for which new jobs will be created
     *
     */
    Set<String> getPublicationIds();
}

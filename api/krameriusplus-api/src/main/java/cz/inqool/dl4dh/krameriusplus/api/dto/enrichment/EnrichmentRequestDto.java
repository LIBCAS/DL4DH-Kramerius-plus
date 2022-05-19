package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "Set of publication UUIDs, for which a job with given configuration will be created.")
    Set<String> getPublicationIds();
}

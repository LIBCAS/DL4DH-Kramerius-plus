package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.JobPlanResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.ExecutionPlanRequestDto;

public interface EnrichmentFacade {

    /**
     * Create and run multiple jobs with the given configuration. For every publicationId, a new job with the given
     * configuration will be created and enqueued. All jobs will be ran asynchronously.
     *
     * @param requestDto request with config to be applied and a set of publicationIds
     * @return dto containing a collection of jobs that were created
     */
    EnrichResponseDto enrich(EnrichmentRequestDto requestDto);

    /**
     * Create and run multiple plans with the given configuration. For every publicationId, a new plan will be created.
     * A plan consists of multiple jobs, that will be launched in the given order. For every configuration in the given
     * list of configurations in requestDto, a new job will be created and placed into the plan. Jobs will be executed
     * one after the other in the given order.
     *
     * @param requestDto
     */
    JobPlanResponseDto enrichWithPlan(ExecutionPlanRequestDto requestDto);
}

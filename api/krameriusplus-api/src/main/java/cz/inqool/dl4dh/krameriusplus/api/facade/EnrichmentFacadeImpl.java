package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.api.dto.EnrichResponseDto;
import cz.inqool.dl4dh.krameriusplus.api.dto.enrichment.SingleJobEnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.EnrichmentRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestSimplifiedCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrichmentFacadeImpl implements EnrichmentFacade {

    private final EnrichmentRequestService enrichmentRequestService;

    private final JobEventService jobEventService;

    @Autowired
    public EnrichmentFacadeImpl(EnrichmentRequestService enrichmentRequestService,
                                JobEventService jobEventService) {
        this.enrichmentRequestService = enrichmentRequestService;
        this.jobEventService = jobEventService;
    }

    @Override
    public EnrichResponseDto enrich(SingleJobEnrichmentRequestDto requestDto) {
        EnrichResponseDto responseDto = new EnrichResponseDto();

        List<JobEventCreateDto> createDtoList = requestDto.getPublicationIds()
                .stream()
                .map(publicationId -> {
                    JobEventCreateDto createDto = new JobEventCreateDto();
                    createDto.setPublicationId(publicationId);
                    createDto.setConfig(requestDto.getConfig());
                    createDto.setJobName(requestDto.getName());

                    return createDto;
                })
                .collect(Collectors.toList());

        responseDto.setEnrichJobs(jobEventService.create(createDtoList));

        enqueueNewJobs(responseDto.getEnrichJobs());

        return responseDto;
    }

    @Override
    public EnrichmentRequestDto enrich(EnrichmentRequestSimplifiedCreateDto createDto) {
        EnrichmentRequestDto resultDto = enrichmentRequestService.create(createDto);

        enrichmentRequestService.startExecution(resultDto);

        return resultDto;
    }

    @Override
    public EnrichmentRequestDto find(String enrichmentRequestId) {
        return enrichmentRequestService.find(enrichmentRequestId);
    }

    @Override
    public List<EnrichmentRequestDto> list() {
        return enrichmentRequestService.listAll();
    }

    private void enqueueNewJobs(List<JobEventDto> createdJobs) {
        for (JobEventDto createdJob : createdJobs) {
            jobEventService.enqueueJob(createdJob);
        }
    }
}

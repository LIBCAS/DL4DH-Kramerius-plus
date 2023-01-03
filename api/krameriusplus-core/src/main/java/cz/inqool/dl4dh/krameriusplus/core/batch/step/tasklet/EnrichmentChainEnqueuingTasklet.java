package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichmentChainEnqueuingTasklet implements Tasklet {

    private final EnrichmentRequest enrichmentRequest;

    private final JobEnqueueService jobEnqueueService;

    @Autowired
    public EnrichmentChainEnqueuingTasklet(@Value("#{jobParameters['" + JobParameterKey.ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                           EnrichmentRequestStore enrichmentRequestStore,
                                           JobEnqueueService jobEnqueueService) {
        this.enrichmentRequest = enrichmentRequestStore.findById(enrichmentRequestId)
                .orElseThrow(() -> new MissingObjectException(EnrichmentRequest.class, enrichmentRequestId));
        this.jobEnqueueService = jobEnqueueService;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        enrichmentRequest.getItems()
                .forEach(enrichmentRequestItem -> enrichmentRequestItem.getEnrichmentChains()
                        .forEach(chain -> jobEnqueueService.enqueue(chain.getJobs().values().iterator().next())));

        return RepeatStatus.FINISHED;
    }
}

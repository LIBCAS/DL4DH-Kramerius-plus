package cz.inqool.dl4dh.krameriusplus.corev2.batch.components;

import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
@StepScope
public class EnrichmentChainEnqueuingTasklet implements Tasklet {

    private final EnrichmentRequest enrichmentRequest;

    private final JobEnqueueService jobEnqueueService;

    public EnrichmentChainEnqueuingTasklet(@Value("#{jobparameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                           EnrichmentRequestStore enrichmentRequestStore,
                                           JobEnqueueService jobEnqueueService) {
        this.enrichmentRequest = enrichmentRequestStore.find(enrichmentRequestId);
        this.jobEnqueueService = jobEnqueueService;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        enrichmentRequest.getItems()
                .forEach(enrichmentRequestItem -> enrichmentRequestItem.getEnrichmentChains()
                        .forEach(chain -> jobEnqueueService.enqueue(chain.getJobs().get(0L))));

        return RepeatStatus.FINISHED;
    }
}

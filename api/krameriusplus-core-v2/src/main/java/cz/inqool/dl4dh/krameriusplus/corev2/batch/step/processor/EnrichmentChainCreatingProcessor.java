package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;


import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItemStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
@StepScope
public class EnrichmentChainCreatingProcessor implements ItemProcessor<List<String>, List<EnrichmentChain>> {

    private Long orderCounter = 0L;

    private final KrameriusJobInstanceService krameriusJobInstanceService;

    private final EnrichmentRequest enrichmentRequest;

    private final EnrichmentRequestItemStore enrichmentRequestItemStore;

    @Autowired
    public EnrichmentChainCreatingProcessor(KrameriusJobInstanceService krameriusJobInstanceService,
                                            EnrichmentRequestStore enrichmentRequestStore,
                                            @Value("#{jobparameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                            EnrichmentRequestItemStore enrichmentRequestItemStore) {
        this.enrichmentRequestItemStore = enrichmentRequestItemStore;
        enrichmentRequest = enrichmentRequestStore.find(enrichmentRequestId);
        this.krameriusJobInstanceService = krameriusJobInstanceService;
    }

    @Override
    public List<EnrichmentChain> process(List<String> item) throws Exception {
        List<EnrichmentChain> chains = item.stream().map(this::createChain).collect(Collectors.toList());

        EnrichmentRequestItem enrichmentRequestItem = findEnrichmentItem(item.get(0));
        enrichmentRequestItem.setEnrichmentChains(chains);
        enrichmentRequestItemStore.update(enrichmentRequestItem);

        return chains;
    }

    private EnrichmentRequestItem findEnrichmentItem(String publicationId) {
        return enrichmentRequest.getItems().stream().filter(item -> item.getPublicationId().equals(publicationId)).findFirst().orElseThrow();
    }

    private EnrichmentChain createChain(String publicationId) {
        EnrichmentChain enrichmentChain = new EnrichmentChain();
        enrichmentChain.setPublicationId(publicationId);
        enrichmentChain.setOrder(orderCounter++);
        enrichmentChain.setJobs(createJobs());

        return enrichmentChain;
    }

    private Map<Long, KrameriusJobInstance> createJobs() {
        Map<Long, KrameriusJobInstance> orderToJobMap = new HashMap<>();

        enrichmentRequest.getConfigs()
                .forEach(config -> orderToJobMap.put(orderCounter++,
                        krameriusJobInstanceService.createJob(config.getJobType(), config.toJobParametersWrapper().getJobParametersMap())));

        return orderToJobMap;
    }
}

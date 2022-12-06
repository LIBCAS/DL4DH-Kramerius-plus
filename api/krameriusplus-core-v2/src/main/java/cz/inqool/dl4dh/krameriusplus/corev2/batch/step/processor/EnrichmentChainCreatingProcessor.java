package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.EnrichmentJobConfig;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChainStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
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
public class EnrichmentChainCreatingProcessor implements ItemProcessor<ChainCreateDto, List<EnrichmentChain>> {

    private final KrameriusJobInstanceService krameriusJobInstanceService;

    private final EnrichmentChainStore enrichmentChainStore;

    private final EnrichmentRequest enrichmentRequest;

    @Autowired
    public EnrichmentChainCreatingProcessor(KrameriusJobInstanceService krameriusJobInstanceService,
                                            EnrichmentRequestStore enrichmentRequestStore,
                                            @Value("#{jobparameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                            EnrichmentChainStore enrichmentChainStore) {
        this.krameriusJobInstanceService = krameriusJobInstanceService;
        this.enrichmentChainStore = enrichmentChainStore;
        this.enrichmentRequest = enrichmentRequestStore.find(enrichmentRequestId);
    }

    @Override
    public List<EnrichmentChain> process(ChainCreateDto item) throws Exception {
        Long chainOrder = 0L;
        List<EnrichmentChain> chains = item.getPublicationIds().stream().map(publicationId ->
                createChain(publicationId, chainOrder)).collect(Collectors.toList());

        EnrichmentRequestItem enrichmentRequestItem = findEnrichmentItem(item.getEnrichmentItemId());
        chains.forEach(chain -> chain.setEnrichmentRequestItem(enrichmentRequestItem));
        chains.forEach(enrichmentChainStore::create);

        return chains;
    }

    private EnrichmentRequestItem findEnrichmentItem(String publicationId) {
        return enrichmentRequest.getItems().stream()
                .filter(item -> item.getPublicationId().equals(publicationId))
                .findFirst().orElseThrow();
    }

    private EnrichmentChain createChain(String publicationId, Long chainOrder) {
        EnrichmentChain enrichmentChain = new EnrichmentChain();
        enrichmentChain.setPublicationId(publicationId);
        enrichmentChain.setOrder(chainOrder);
        enrichmentChain.setJobs(createJobs());

        return enrichmentChain;
    }

    private Map<Long, KrameriusJobInstance> createJobs() {
        Map<Long, KrameriusJobInstance> orderToJobMap = new HashMap<>();
        long orderCounter = 0L;

        for (EnrichmentJobConfig config : enrichmentRequest.getConfigs()) {
            orderToJobMap.put(orderCounter++, krameriusJobInstanceService.createJob(config.getJobType(),
                    config.toJobParametersWrapper().getJobParametersMap()));
        }

        return orderToJobMap;
    }
}

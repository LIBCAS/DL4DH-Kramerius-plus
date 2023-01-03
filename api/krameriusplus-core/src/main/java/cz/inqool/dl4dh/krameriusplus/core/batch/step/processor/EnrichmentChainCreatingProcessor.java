package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.ChainCreateWrapper;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.job.config.enrichment.EnrichmentJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChain;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainStore;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@StepScope
public class EnrichmentChainCreatingProcessor implements ItemProcessor<ChainCreateWrapper, List<EnrichmentChain>> {

    private final KrameriusJobInstanceService krameriusJobInstanceService;

    private final EnrichmentChainStore enrichmentChainStore;

    private final EnrichmentRequest enrichmentRequest;

    @Autowired
    public EnrichmentChainCreatingProcessor(KrameriusJobInstanceService krameriusJobInstanceService,
                                            EnrichmentRequestStore enrichmentRequestStore,
                                            @Value("#{jobParameters['" + JobParameterKey.ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                            EnrichmentChainStore enrichmentChainStore) {
        this.krameriusJobInstanceService = krameriusJobInstanceService;
        this.enrichmentChainStore = enrichmentChainStore;
        this.enrichmentRequest = enrichmentRequestStore.findById(enrichmentRequestId)
                .orElseThrow(() -> new MissingObjectException(EnrichmentRequest.class, enrichmentRequestId));
    }

    @Override
    public List<EnrichmentChain> process(ChainCreateWrapper item) throws Exception {
        long chainOrder = 0L;
        List<EnrichmentChain> chains = new ArrayList<>();
        for (ChainCreateWrapper.PublicationData publicationData : item.getPublications()) {
            chains.add(createChain(publicationData, chainOrder++));
        }

        EnrichmentRequestItem enrichmentRequestItem = findEnrichmentItem(item.getEnrichmentItemId());
        chains.forEach(chain -> chain.setRequestItem(enrichmentRequestItem));

        return enrichmentChainStore.saveAll(chains);
    }

    private EnrichmentRequestItem findEnrichmentItem(String publicationId) {
        return enrichmentRequest.getItems().stream()
                .filter(item -> item.getPublicationId().equals(publicationId))
                .findFirst().orElseThrow();
    }

    private EnrichmentChain createChain(ChainCreateWrapper.PublicationData publicationData, Long chainOrder) {
        EnrichmentChain enrichmentChain = new EnrichmentChain();
        enrichmentChain.setPublicationId(publicationData.getPublicationId());
        enrichmentChain.setPublicationTitle(publicationData.getPublicationTitle());
        enrichmentChain.setModel(publicationData.getModel());
        enrichmentChain.setOrder(chainOrder);
        enrichmentChain.setJobs(createJobs(publicationData.getPublicationId()));

        return enrichmentChain;
    }

    private Map<Long, KrameriusJobInstance> createJobs(String publicationId) {
        Map<Long, KrameriusJobInstance> orderToJobMap = new HashMap<>();
        long orderCounter = 0L;

        for (EnrichmentJobConfig config : enrichmentRequest.getConfigs()) {
            JobParametersMapWrapper jobParametersMapWrapper = config.toJobParametersWrapper();
            jobParametersMapWrapper.putString(JobParameterKey.PUBLICATION_ID, publicationId);

            orderToJobMap.put(orderCounter++, krameriusJobInstanceService.createJobInstance(
                    config.getJobType(),
                    jobParametersMapWrapper));
        }

        return orderToJobMap;
    }
}

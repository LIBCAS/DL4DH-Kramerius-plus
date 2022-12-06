package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.CREATE_ENRICHMENT_REQUEST;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.*;

@Configuration
public class CreateEnrichmentRequestJobDesigner extends AbstractJobDesigner {

    private Step savePublicationsStep;

    private Step createEnrichmentItemsStep;

    private Step createEnrichmentChainsStep;

    private Step enqueueEnrichmentChainsStep;

    @Override
    public String getJobName() {
        return CREATE_ENRICHMENT_REQUEST;
    }

    @Bean(CREATE_ENRICHMENT_REQUEST)
    @Override
    public Job build() {
        // Step 1 - save publications
        //  - ItemReader - reads UUIDs from EnrichmentRequest
        //  - ItemProcessor - <String, Publication> - fetches publication tree from Kramerius
        //  - ItemWriter - save publication tree to MongoDB
        // Step 2 - create EnrichmentRequestItems
        //  - ItemReader - read UUIDs from EnrichmentRequest
        //  - ItemProcessor - <String, EnrichmentRequestItem> - creates an EnrichmentRequestItem for every publicationId in EnrichmentRequest
        //  - ItemProcessor - save EnrichmentRequestItem to DB
        // Step 3 - create EnrichmentChains
        //  - ItemReader - read UUIDs from EnrichmentRequest
        //  - ItemProcessor - delegate item processor
        //      - ItemProcessor<String, List<String>> - transforms input UUID to list of UUIDs of every child in
        //      subtree (using CustomPublicationStore::findAllEditions)
        //      - ItemProcessor<List<String>, List<EnrichmentChain>> - creates an EnrichmentChain for every uuid
        //          - for every UUID in input, do:
        //              - for every EnrichmentJobConfig in EnrichmentRequest, do: (order of configs is important)
        //                  - create KrameriusJobInstance from UUID + EnrichmentJobConfig (using KrameriusJobInstanceService::createJob)
        //                      - get jobType from config, get jobParametersMap from EnrichmentJobConfig
        //                  - create EnrichmentChain with created KrameriusJobInstances
        //      - ItemWriter<List<EnrichmentChain>> - save to DB
        // Step 4 - enqueue EnrichmentChains
        //  - Tasklet - fetch all EnrichmentRequestItems
        //      - for each EnrichmentRequestItem, do
        //          - for each EnrichmentChain, do
        //              - enqueue first job in chain
        return getJobBuilder().start(savePublicationsStep)
                .next(createEnrichmentItemsStep)
                .next(createEnrichmentChainsStep)
                .next(enqueueEnrichmentChainsStep)
                .build();
    }

    @Autowired
    public void setSavePublicationsStep(@Qualifier(FETCH_PUBLICATIONS) Step savePublicationsStep) {
        this.savePublicationsStep = savePublicationsStep;
    }

    @Autowired
    public void setCreateEnrichmentItemsStep(@Qualifier(CREATE_ENRICHMENT_ITEMS) Step createEnrichmentItemsStep) {
        this.createEnrichmentItemsStep = createEnrichmentItemsStep;
    }

    @Autowired
    public void setCreateEnrichmentChainsStep(@Qualifier(CREATE_ENRICHMENT_CHAINS) Step createEnrichmentChainsStep) {
        this.createEnrichmentChainsStep = createEnrichmentChainsStep;
    }

    @Autowired
    public void setEnqueueEnrichmentChainsStep(@Qualifier(ENQUEUE_CHAINS) Step enqueueEnrichmentChainsStep) {
        this.enqueueEnrichmentChainsStep = enqueueEnrichmentChainsStep;
    }
}

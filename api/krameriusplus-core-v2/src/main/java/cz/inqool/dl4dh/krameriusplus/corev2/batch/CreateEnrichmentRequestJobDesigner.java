package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.CREATE_ENRICHMENT_REQUEST;

@Component
public class CreateEnrichmentRequestJobDesigner extends AbstractJobDesigner {

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
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}

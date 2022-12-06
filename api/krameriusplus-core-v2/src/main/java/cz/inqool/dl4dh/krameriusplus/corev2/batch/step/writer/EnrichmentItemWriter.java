package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItemStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
@StepScope
public class EnrichmentItemWriter implements ItemWriter<EnrichmentRequestItem> {

    private final EnrichmentRequestItemStore enrichmentRequestItemStore;

    private final EnrichmentRequest enrichmentRequest;

    private final EnrichmentRequestStore enrichmentRequestStore;

    @Autowired
    public EnrichmentItemWriter(@Value("#{jobparameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                EnrichmentRequestItemStore enrichmentRequestItemStore, EnrichmentRequestStore enrichmentRequestStore) {
        this.enrichmentRequestItemStore = enrichmentRequestItemStore;
        this.enrichmentRequest = enrichmentRequestStore.find(enrichmentRequestId);
        this.enrichmentRequestStore = enrichmentRequestStore;
    }

    @Override
    public void write(List<? extends EnrichmentRequestItem> items) throws Exception {
        enrichmentRequest.setItems((List<EnrichmentRequestItem>) items);
        enrichmentRequestStore.update(enrichmentRequest);
        enrichmentRequestItemStore.create(items);
    }
}

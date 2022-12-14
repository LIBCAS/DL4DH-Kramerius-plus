package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
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

    @Autowired
    public EnrichmentItemWriter(@Value("#{jobParameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                EnrichmentRequestItemStore enrichmentRequestItemStore, EnrichmentRequestStore enrichmentRequestStore) {
        this.enrichmentRequestItemStore = enrichmentRequestItemStore;
        this.enrichmentRequest = enrichmentRequestStore.findById(enrichmentRequestId)
                .orElseThrow(() -> new MissingObjectException(EnrichmentRequest.class, enrichmentRequestId));
    }

    @Override
    public void write(List<? extends EnrichmentRequestItem> items) {
        items.forEach(item -> item.setEnrichmentRequest(enrichmentRequest));
        enrichmentRequestItemStore.saveAll(items);
    }
}

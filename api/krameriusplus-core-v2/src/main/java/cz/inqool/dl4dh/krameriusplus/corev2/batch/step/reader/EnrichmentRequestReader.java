package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
@StepScope
public class EnrichmentRequestReader implements ItemReader<String> {

    private final EnrichmentRequestStore enrichmentRequestStore;

    private final String enrichmentRequestId;

    private List<String> publicationIds;

    @Autowired
    public EnrichmentRequestReader(@Value("#{jobparameters['" + ENRICHMENT_REQUEST_ID + "']}") String enrichmentRequestId,
                                   EnrichmentRequestStore enrichmentRequestStore) {
        this.enrichmentRequestStore = enrichmentRequestStore;
        this.enrichmentRequestId = enrichmentRequestId;
    }

    @Override
    public String read() {
        if (publicationIds == null) {
            publicationIds = new ArrayList<>();
            publicationIds.addAll(new TreeMap<>(enrichmentRequestStore.find(enrichmentRequestId).getPublicationIds()).values());
        }

        return publicationIds.isEmpty() ? null : publicationIds.remove(0);
    }
}

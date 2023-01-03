package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequestStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.QEnrichmentRequest;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.ENRICHMENT_REQUEST_ID;

@Component
@StepScope
public class EnrichmentRequestReader extends RequestPublicationIdReader<EnrichmentRequest, QEnrichmentRequest> {

    @Getter
    private EnrichmentRequestStore requestStore;

    @Getter
    @Value("#{jobParameters['" + ENRICHMENT_REQUEST_ID + "']}")
    private String requestId;

    @Autowired
    public void setRequestStore(EnrichmentRequestStore requestStore) {
        this.requestStore = requestStore;
    }
}

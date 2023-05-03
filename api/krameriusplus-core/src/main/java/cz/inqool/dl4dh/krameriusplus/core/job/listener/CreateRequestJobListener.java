package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceStore;
import cz.inqool.dl4dh.krameriusplus.core.request.Request;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request.EnrichmentRequestStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.RequestState.RUNNING;
import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.CREATE_ENRICHMENT_REQUEST;
import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.CREATE_EXPORT_REQUEST;

@RequiredArgsConstructor
@Component
public class CreateRequestJobListener implements KrameriusJobListener {

    private final ExportRequestStore exportRequestStore;

    private final EnrichmentRequestStore enrichmentRequestStore;

    private final KrameriusJobInstanceStore jobInstanceStore;

    @Override
    @Transactional
    public void beforeJob(String jobInstanceId) {
        KrameriusJobInstance jobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));

        EnrichmentRequest request = enrichmentRequestStore.findByCreateRequestJob(jobInstance);

        request.setState(RUNNING);
    }

    @Override
    @Transactional
    public void afterJob(String jobInstanceId) {
        // get up-to-date data
        KrameriusJobInstance jobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));

        // if Create___Request job fails, set RequestState to FAILED
        if (ExecutionStatus.FAILED.equals(jobInstance.getExecutionStatus()) ||
                ExecutionStatus.FAILED_FATALLY.equals(jobInstance.getExecutionStatus())) {
            Request request = findRequest(jobInstance);

            request.setState(RequestState.FAILED);

            saveRequest(request);
        }
    }

    private void saveRequest(Request request) {
        if (request instanceof EnrichmentRequest) {
            enrichmentRequestStore.save((EnrichmentRequest) request);
        } else if (request instanceof ExportRequest) {
            exportRequestStore.save((ExportRequest) request);
        }
    }

    private Request findRequest(KrameriusJobInstance jobInstance) {
        if (CREATE_ENRICHMENT_REQUEST.equals(jobInstance.getJobType())) {
            return enrichmentRequestStore.findByCreateRequestJob(jobInstance);
        } else if (CREATE_EXPORT_REQUEST.equals(jobInstance.getJobType())) {
            return exportRequestStore.findByCreateRequestJob(jobInstance);
        }

        throw new IllegalStateException("Unexpected jobType in CreateRequestJobListener: " + jobInstance.getJobType());
    }

    @Override
    public boolean supports(KrameriusJobInstance jobInstance) {
        return List.of(CREATE_ENRICHMENT_REQUEST, CREATE_EXPORT_REQUEST).contains(jobInstance.getJobType());
    }
}

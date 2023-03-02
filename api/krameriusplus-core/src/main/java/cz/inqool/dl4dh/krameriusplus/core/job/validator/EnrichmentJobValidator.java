package cz.inqool.dl4dh.krameriusplus.core.job.validator;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceStore;
import cz.inqool.dl4dh.krameriusplus.core.job.LastLaunch;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainStore;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.exception.JobException.ErrorCode.IS_COMPLETED;
import static cz.inqool.dl4dh.krameriusplus.api.exception.JobException.ErrorCode.IS_RUNNING;
import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.*;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

/**
 * Validator to see if enrichment job can be run in context of other jobs
 */
@Component
public class EnrichmentJobValidator implements JobParametersValidator {

    private EnrichmentChainStore enrichmentChainStore;

    private KrameriusJobInstanceStore krameriusJobInstanceStore;

    /**
     * First validate that parameters contain necessary keys,
     * then validate if the new job instance can be run
     *
     * @param parameters some {@link JobParameters} (can be {@code null})
     * @throws JobParametersInvalidException in case of parameters missing needed keys
     */
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        notNull(parameters, () -> new IllegalArgumentException("JobParameters are null"));

        KrameriusJobInstance newJob = checkKeys(parameters);
        String publicationId = parameters.getString(PUBLICATION_ID);

        List<KrameriusJobInstance> matchingJobs = enrichmentChainStore.findMatchingJobs(publicationId, newJob.getJobType());

        validateNoRunningJobs(matchingJobs, publicationId, newJob);
        validateOverride(matchingJobs, parameters, publicationId, newJob);

        // job ok to run
        LastLaunch lastLaunch = new LastLaunch();
        newJob.setLastLaunch(lastLaunch);
        krameriusJobInstanceStore.save(newJob);
    }

    private void validateOverride(List<KrameriusJobInstance> matchingJobs, JobParameters parameters,
                                  String publicationId, KrameriusJobInstance newJob) {
        boolean override = Boolean.parseBoolean(parameters.getString(OVERRIDE));

        KrameriusJobInstance completedJob = matchingJobs.stream()
                .filter(krameriusJobInstance -> krameriusJobInstance.getExecutionStatus().equals(ExecutionStatus.COMPLETED))
                .findFirst().orElse(null);

        // if there is a completed job override needs to be set to true
        if (completedJob != null && !override) {
            throw new JobException(newJob.getId(),
                    String.format("KrameriusJobInstance of type: %s, for publicationId: %s is already completed with ID: %s and `override` is set to false.",
                            newJob.getJobType(), publicationId, completedJob.getId()),
                    IS_COMPLETED);
        }
    }

    private void validateNoRunningJobs(List<KrameriusJobInstance> matchingJobs, String publicationId, KrameriusJobInstance newJob) {
        KrameriusJobInstance runningJob = matchingJobs.stream()
                .filter(krameriusJobInstance -> !krameriusJobInstance.getExecutionStatus().finished())
                .filter(krameriusJobInstance -> !krameriusJobInstance.getId().equals(newJob.getId()))
                .findFirst().orElse(null);

        if (runningJob != null) {
            throw new JobException(newJob.getId(),
                    String.format("KrameriusJobInstance of type: %s, for publicationId: %s is already running with ID: %s.",
                            newJob.getJobType(), publicationId, runningJob.getId()),
                    IS_RUNNING);
        }
    }

    /**
     * Validate that context has all necessary keys, then fetch needed job instance
     *
     * @param parameters to validate
     * @return KrameriusJobInstance specified in params
     * @throws JobParametersInvalidException in case of a missing key
     */
    private KrameriusJobInstance checkKeys(JobParameters parameters) throws JobParametersInvalidException {
        // Can't set pre-launch status if job instance key is missing
        String krameriusJobInstanceId = parameters.getString(KRAMERIUS_JOB_INSTANCE_ID);
        if (krameriusJobInstanceId == null) {
            throw new JobParametersInvalidException("Job is missing entry in JobParameters for KrameriusJobInstanceId");
        }

        KrameriusJobInstance newJob = krameriusJobInstanceStore.findById(krameriusJobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, krameriusJobInstanceId));

        List<String> missingParameters = new ArrayList<>();

        if (!parameters.getParameters().containsKey(OVERRIDE)) {
            missingParameters.add(OVERRIDE);
        }
        if (!parameters.getParameters().containsKey(PUBLICATION_ID)) {
            missingParameters.add(PUBLICATION_ID);
        }

        // in case keys are missing create status save and throw exception
        if (!missingParameters.isEmpty()) {
            String message = "In KrameriusJobInstance with ID: " + krameriusJobInstanceId + " the following parameters are missing: "
                    + String.join(", ", missingParameters);

            throw new JobParametersInvalidException(message);
        }

        return newJob;
    }

    @Autowired
    public void setEnrichmentChainStore(EnrichmentChainStore enrichmentChainStore) {
        this.enrichmentChainStore = enrichmentChainStore;
    }

    @Autowired
    public void setKrameriusJobInstanceStore(KrameriusJobInstanceStore krameriusJobInstanceStore) {
        this.krameriusJobInstanceStore = krameriusJobInstanceStore;
    }
}

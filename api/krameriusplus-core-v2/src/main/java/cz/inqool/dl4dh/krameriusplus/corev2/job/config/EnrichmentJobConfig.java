package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request.EnrichmentRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.OVERRIDE;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PAGE_ERROR_TOLERANCE;

@Getter
@Setter
@Entity
@Table(name = "kplus_enrichment_job_config")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EnrichmentJobConfig extends JobConfig {

    private boolean override = false;

    private Long pageErrorTolerance = 0L;

    @ManyToOne
    private EnrichmentRequest enrichmentRequest;

    @Override
    public JobParametersMapWrapper toJobParametersWrapper() {
        JobParametersMapWrapper jobParametersMap = super.toJobParametersWrapper();
        jobParametersMap.putBoolean(OVERRIDE, override);
        jobParametersMap.putLong(PAGE_ERROR_TOLERANCE, pageErrorTolerance);

        return jobParametersMap;
    }
}

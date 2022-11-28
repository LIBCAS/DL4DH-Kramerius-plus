package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put(OVERRIDE, Boolean.valueOf(override).toString());
        jobParametersMap.put(PAGE_ERROR_TOLERANCE, pageErrorTolerance);

        return jobParametersMap;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.job.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.JOB_CONFIG_ID;

@Getter
@Setter
@MappedSuperclass
public abstract class JobConfig extends DomainObject {

    public abstract KrameriusJobType getJobType();

    public JobParametersMapWrapper toJobParametersWrapper() {
        JobParametersMapWrapper jobParametersMapWrapper = new JobParametersMapWrapper();
        jobParametersMapWrapper.putString(JOB_CONFIG_ID, this.getId());

        return jobParametersMapWrapper;
    }
}

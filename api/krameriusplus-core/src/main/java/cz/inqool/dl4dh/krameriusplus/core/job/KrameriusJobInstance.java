package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepRunReport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobParameters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "kplus_kramerius_job_instance")
public class KrameriusJobInstance extends DomainObject {

    @NotNull
    @Enumerated(EnumType.STRING)
    protected ExecutionStatus executionStatus = ExecutionStatus.CREATED;

    protected Long jobInstanceId;

    @OneToMany(mappedBy = "job")
    @MapKey(name = "stepExecutionId")
    private Map<Long, StepRunReport> reports = new HashMap<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusJobType jobType;

    @Convert(converter = JobParametersJsonConverter.class)
    private JobParameters jobParameters;

    @Embedded
    private LastLaunch lastLaunch = new LastLaunch();
}

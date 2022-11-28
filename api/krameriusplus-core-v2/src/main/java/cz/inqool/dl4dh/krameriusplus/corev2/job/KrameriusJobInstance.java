package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepRunReport;
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

    @Enumerated(EnumType.STRING)
    protected ExecutionStatus executionStatus;

    protected Long jobInstanceId;

    @OneToMany
    private Map<Long, StepRunReport> reports = new HashMap<>();

    @NotNull
    private KrameriusJobType jobType;

    @Convert(converter = JobParametersJsonConverter.class)
    private JobParameters jobParameters;
}

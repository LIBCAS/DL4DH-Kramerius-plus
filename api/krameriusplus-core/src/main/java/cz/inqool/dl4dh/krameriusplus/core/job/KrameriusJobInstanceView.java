package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_kramerius_job_instance")
public class KrameriusJobInstanceView extends DomainObject {

    @NotNull
    @Enumerated(EnumType.STRING)
    protected ExecutionStatus executionStatus = ExecutionStatus.CREATED;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusJobType jobType;
}

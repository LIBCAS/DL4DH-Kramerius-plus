package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class ScheduledJobEvent extends DatedObject {

    @NotNull
    @Column(name = "execution_order")
    private Integer order;

    @OneToOne
    private JobEvent jobEvent;

    @ManyToOne
    private JobPlan jobPlan;
}

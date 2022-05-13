package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
public class JobEvent extends DatedObject {

    private String jobName;

    private String publicationId;

    @Column(unique = true)
    private Long instanceId;

    private Long lastExecutionId;

    @Enumerated(EnumType.STRING)
    private JobStatus lastExecutionStatus = JobStatus.CREATED;

    @ManyToOne
    private JobEvent parent;

    @Embedded
    private JobEventConfig config;

    public boolean wasExecuted() {
        return lastExecutionId != null;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
@ToString
public class LastExecutionDetails {
    private Long lastExecutionId;

    private Throwable lastExecutionError;

    @Enumerated(EnumType.STRING)
    private JobStatus lastExecutionStatus = JobStatus.CREATED;
}

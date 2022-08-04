package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@Setter
public class ExecutionDetails {

    @JsonIgnore
    @NotNull
    private Long lastExecutionId;

    private String lastExecutionExitCode;

    private String lastExecutionExitDescription;

    @Enumerated(EnumType.STRING)
    private JobStatus lastExecutionStatus = JobStatus.CREATED;

    private String runErrorMessage;

    private String runErrorStackTrace;
}

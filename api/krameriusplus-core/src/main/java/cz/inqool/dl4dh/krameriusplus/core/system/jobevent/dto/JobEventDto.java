package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.BatchStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class JobEventDto extends DatedObjectDto {

    private String jobName;

    private String publicationId;

    @JsonIgnore
    private Long instanceId;

    @JsonIgnore
    private Long lastExecutionId;

    private BatchStatus lastExecutionStatus;

    private JobEvent parent;

    private KrameriusJob krameriusJob;

    private Map<String, Object> parameters = new HashMap<>();

    private List<JobExecutionDto> executions = new ArrayList<>();
}

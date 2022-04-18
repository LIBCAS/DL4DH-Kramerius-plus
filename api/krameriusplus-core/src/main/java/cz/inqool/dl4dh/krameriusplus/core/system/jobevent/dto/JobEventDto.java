package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.job.dto.JobExecutionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JobEventDto extends DatedObjectDto {

    private String jobName;

    private String publicationId;

    private Map<String, Object> parameters;

    private List<JobExecutionDto> executions = new ArrayList<>();
}

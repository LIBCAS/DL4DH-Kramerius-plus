package cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class JobEventDetailDto extends JobEventDto {

    private List<JobExecutionDto> executions = new ArrayList<>();
}

package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class KrameriusJobInstanceDto extends DomainObjectDto {

    private ExecutionStatus executionStatus;

    private List<JobExecutionDto> executions = new ArrayList<>();

    private KrameriusJobType jobType;

    private Map<String, Object> jobParameters = new HashMap<>();
}

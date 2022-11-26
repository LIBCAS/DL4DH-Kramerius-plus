package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class KrameriusJobDto {

    private ExecutionStatus jobStatus;

    private List<JobExecutionDto> executions = new ArrayList<>();
}

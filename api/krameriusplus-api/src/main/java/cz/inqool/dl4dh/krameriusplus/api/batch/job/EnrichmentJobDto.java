package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichmentJobDto {

    private EnrichmentJobConfigDto config;

    private List<JobExecutionDto> executions = new ArrayList<>();

    private ExecutionStatus status;
}

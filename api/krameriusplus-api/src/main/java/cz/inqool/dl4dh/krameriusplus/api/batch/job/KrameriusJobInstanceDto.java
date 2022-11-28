package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KrameriusJobInstanceDto extends DomainObjectDto {

    private ExecutionStatus jobStatus;

    private List<JobExecutionDto> executions = new ArrayList<>();

    private KrameriusJobType jobType;
}

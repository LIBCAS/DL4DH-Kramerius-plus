package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrameriusJobInstanceGridDto extends DomainObjectDto {

    private ExecutionStatus executionStatus;

    private KrameriusJobType jobType;
}

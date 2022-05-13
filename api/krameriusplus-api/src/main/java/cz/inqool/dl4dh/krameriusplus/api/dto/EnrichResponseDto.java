package cz.inqool.dl4dh.krameriusplus.api.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EnrichResponseDto {

    private Set<JobEventDto> enrichJobs = new HashSet<>();
}

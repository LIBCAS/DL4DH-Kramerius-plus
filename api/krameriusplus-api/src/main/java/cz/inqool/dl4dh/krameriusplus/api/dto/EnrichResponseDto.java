package cz.inqool.dl4dh.krameriusplus.api.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnrichResponseDto {

    private List<JobEventDto> enrichJobs = new ArrayList<>();
}

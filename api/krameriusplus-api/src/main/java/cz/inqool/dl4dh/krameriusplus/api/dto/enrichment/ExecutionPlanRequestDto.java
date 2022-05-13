package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ExecutionPlanRequestDto {

    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();

    @NotEmpty
    private List<JobEventConfigCreateDto> configs = new ArrayList<>();
}

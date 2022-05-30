package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Schema(description = "DTO for creating JobPlans.")
public class JobPlanRequestDto {

    @Schema(description = "Set of publicationIds")
    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();

    @NotEmpty
    private List<JobEventConfigCreateDto> configs = new ArrayList<>();
}

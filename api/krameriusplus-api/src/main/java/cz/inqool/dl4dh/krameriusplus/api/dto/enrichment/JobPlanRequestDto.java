package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment.EnrichmentJobConfigDto;
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

    @Schema(description = "Optional name. The name will be used as the name of the created JobPlan, " +
            "as well as the name of all the created JobEvents.")
    private String name;

    @Schema(description = "Set of publicationIds")
    @NotEmpty
    private Set<String> publicationIds = new HashSet<>();

    @NotEmpty
    private List<EnrichmentJobConfigDto> configs = new ArrayList<>();
}

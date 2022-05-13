package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.DownloadKStructureJobConfigDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DownloadKStructureRequestDto implements EnrichmentRequestDto {

    @NotEmpty
    @Schema(description = "Set of publication UUIDs, for which a job with given configuration will be created.")
    private Set<String> publicationIds = new HashSet<>();

    @NotNull
    private DownloadKStructureJobConfigDto config;

    @Schema(description = "If true and publications already exist, they will be overwritten. Defaults to false.")
    private boolean override = false;
}

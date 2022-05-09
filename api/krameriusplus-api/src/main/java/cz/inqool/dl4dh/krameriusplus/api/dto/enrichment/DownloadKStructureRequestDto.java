package cz.inqool.dl4dh.krameriusplus.api.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DownloadKStructureRequestDto {

    @NotEmpty
    @Schema(description = "Set of jobs of type DOWNLOAD_K_STRUCTURE that should be created")
    private Set<@Valid DownloadKStructureCreateDto> publications = new HashSet<>();

    @Schema(description = "If true and publications already exist, they will be overwritten. Defaults to false.")
    private boolean override = false;
}

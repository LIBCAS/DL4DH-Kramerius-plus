package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "DTO for creating jobs of type DOWNLOAD_K_STRUCTURE")
public class DownloadKStructureCreateDto extends JobEventCreateDto {

    private final KrameriusJob krameriusJob = KrameriusJob.DOWNLOAD_K_STRUCTURE;
}

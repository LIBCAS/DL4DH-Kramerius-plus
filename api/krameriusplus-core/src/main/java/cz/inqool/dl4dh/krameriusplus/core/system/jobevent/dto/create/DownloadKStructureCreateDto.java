package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import lombok.Getter;

@Getter
public class DownloadKStructureCreateDto extends JobEventCreateDto {

    private final KrameriusJob krameriusJob = KrameriusJob.DOWNLOAD_K_STRUCTURE;
}

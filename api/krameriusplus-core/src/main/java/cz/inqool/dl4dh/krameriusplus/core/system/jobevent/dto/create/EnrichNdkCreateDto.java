package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import lombok.Getter;

@Getter
public class EnrichNdkCreateDto extends JobEventCreateDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICH_NDK;
}

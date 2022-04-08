package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.KrameriusJob;
import lombok.Getter;

@Getter
public class EnrichingJobEventCreateDto extends JobEventCreateDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHING_JOB;
}

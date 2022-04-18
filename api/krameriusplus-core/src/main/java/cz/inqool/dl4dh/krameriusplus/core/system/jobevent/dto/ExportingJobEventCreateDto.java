package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportingJobEventCreateDto extends JobEventCreateDto {

    private String publicationTitle;

    private Params params;

    private ExportFormat exportFormat;

    @Setter(AccessLevel.NONE)
    private KrameriusJob krameriusJob = KrameriusJob.EXPORTING_JOB;
}

package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkExportDto {

    private KrameriusJobInstanceDto mergeJob;

    private FileRefDto file;

    private BulkExportState state;
}

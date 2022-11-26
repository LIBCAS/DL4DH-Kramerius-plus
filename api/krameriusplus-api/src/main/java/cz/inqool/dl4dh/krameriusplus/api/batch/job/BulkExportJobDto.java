package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BulkExportJobDto {

    private FileRefDto fileRef;

    private List<ExportJobDto> exportJobs = new ArrayList<>();

    private ExportFormat exportFormat;

    private MergeJobDto mergeJob;
}

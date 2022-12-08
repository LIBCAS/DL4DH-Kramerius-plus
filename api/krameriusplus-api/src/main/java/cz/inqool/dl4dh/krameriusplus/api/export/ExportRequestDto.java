package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.RequestDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.ExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportRequestDto extends RequestDto {

    private ExportJobConfigDto config;

    private List<ExportRequestItemDto> items = new ArrayList<>();

    private KrameriusJobInstanceDto createRequestJob;

    private BulkExportDto bulkExport;
}

package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.SingleExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;

public interface ExportFacade {

    ExportRequestDto export(SingleExportRequestDto requestDto);

    QueryResults<Export> list(String publicationId, int page, int pageSize);

    FileRef getFile(String fileRefId);

    BulkExportDto findBulkExport(String jobEventId);
}

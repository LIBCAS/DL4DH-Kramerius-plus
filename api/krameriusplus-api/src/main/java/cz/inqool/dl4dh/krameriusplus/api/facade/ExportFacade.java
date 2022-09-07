package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;

public interface ExportFacade {

    BulkExportDto export(ExportRequestDto requestDto);

    QueryResults<Export> list(String publicationId, int page, int pageSize);

    FileRef getFile(String fileRefId);

    ExportDto findByJobEvent(String jobEventId);

    BulkExportDto findBulkExport(String jobEventId);
}

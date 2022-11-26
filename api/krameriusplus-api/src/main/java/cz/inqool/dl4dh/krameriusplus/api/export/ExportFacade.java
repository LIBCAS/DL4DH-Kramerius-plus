package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.QueryResults;

public interface ExportFacade {

    ExportRequestDto export(ExportRequestCreateDto requestDto);

    ExportRequestDto find(String exportRequestId);

    QueryResults<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize);
}

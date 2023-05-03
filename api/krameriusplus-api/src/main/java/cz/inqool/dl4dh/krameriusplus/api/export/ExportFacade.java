package cz.inqool.dl4dh.krameriusplus.api.export;

import cz.inqool.dl4dh.krameriusplus.api.Result;

public interface ExportFacade {

    ExportRequestDto export(ExportRequestCreateDto requestDto);

    ExportRequestDto find(String exportRequestId);

    Result<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize);

    ExportRequestDto cancel(String id);
}

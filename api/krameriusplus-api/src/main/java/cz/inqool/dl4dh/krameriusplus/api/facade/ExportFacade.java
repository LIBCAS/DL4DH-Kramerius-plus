package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestDto;

public interface ExportFacade {

    ExportRequestDto export(ExportRequestCreateDto requestDto);

    ExportRequestDto find(String exportRequestId);

    QueryResults<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize);
}

package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;

import java.util.List;

public interface ExportFacade {

    ExportRequestDto export(ExportRequestCreateDto requestDto);

    ExportRequestDto find(String exportRequestId);

    List<ExportRequestDto> listAll();
}

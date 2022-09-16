package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.ExportRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportFacadeImpl implements ExportFacade {

    private final ExportRequestService exportRequestService;

    @Autowired
    public ExportFacadeImpl(ExportRequestService exportRequestService) {
        this.exportRequestService = exportRequestService;
    }

    @Override
    public ExportRequestDto export(ExportRequestCreateDto requestDto) {
        ExportRequestDto exportRequestDto = exportRequestService.create(requestDto);

        exportRequestService.startExecution(exportRequestDto);

        return exportRequestDto;
    }

    @Override
    public ExportRequestDto find(String exportRequestId) {
        return exportRequestService.find(exportRequestId);
    }

    @Override
    public QueryResults<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        return exportRequestService.list(name, owner, isFinished, page, pageSize);
    }
}

package cz.inqool.dl4dh.krameriusplus.api.facade;

import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<ExportRequestDto> listAll() {
        return exportRequestService.listAll();
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestFacade implements ExportFacade {

    private final ExportRequestService exportRequestService;

    @Autowired
    public ExportRequestFacade(ExportRequestService exportRequestService) {
        this.exportRequestService = exportRequestService;
    }

    @Override
    public ExportRequestDto export(ExportRequestCreateDto requestDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public ExportRequestDto find(String exportRequestId) {
        return exportRequestService.find(exportRequestId);
    }

    @Override
    public Result<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        return exportRequestService.listByNameOwnerAndStatus(name, owner, isFinished, page, pageSize);
    }
}

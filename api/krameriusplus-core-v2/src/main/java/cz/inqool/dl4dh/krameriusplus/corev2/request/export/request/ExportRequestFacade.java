package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestFacade implements ExportFacade {

    private ExportRequestService exportRequestService;

    private JobEnqueueService jobEnqueueService;

    @Override
    public ExportRequestDto export(ExportRequestCreateDto requestDto) {
        ExportRequestDto exportRequestDto = exportRequestService.create(requestDto);

        jobEnqueueService.enqueue(exportRequestDto.getCreateRequestJob());

        return exportRequestDto;
    }

    @Override
    public ExportRequestDto find(String exportRequestId) {
        return exportRequestService.find(exportRequestId);
    }

    @Override
    public Result<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        return exportRequestService.list(name, owner, isFinished, page, pageSize);
    }

    @Autowired
    public void setExportRequestService(ExportRequestService exportRequestService) {
        this.exportRequestService = exportRequestService;
    }

    @Autowired
    public void setJobEnqueueService(JobEnqueueService jobEnqueueService) {
        this.jobEnqueueService = jobEnqueueService;
    }
}

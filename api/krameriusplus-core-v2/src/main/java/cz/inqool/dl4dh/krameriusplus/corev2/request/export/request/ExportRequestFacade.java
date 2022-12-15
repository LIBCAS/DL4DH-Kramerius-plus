package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportFacade;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ExportRequestFacade implements ExportFacade {

    private final ExportRequestService exportRequestService;

    private final JobEnqueueService jobEnqueueService;

    @Autowired
    public ExportRequestFacade(ExportRequestService exportRequestService, JobEnqueueService jobEnqueueService) {
        this.exportRequestService = exportRequestService;
        this.jobEnqueueService = jobEnqueueService;
    }

    @Override
    @Transactional
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
}

package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExporterFacadeImpl implements ExporterFacade {

    private final JobEventService jobEventService;

    private final ExportService exportService;

    private final FileService fileService;

    @Autowired
    public ExporterFacadeImpl(JobEventService jobEventService, ExportService exportService, FileService fileService) {
        this.jobEventService = jobEventService;
        this.exportService = exportService;
        this.fileService = fileService;
    }

    @Override
    public JobEventDto export(ExportRequestDto requestDto) {
        return createJob(requestDto);
    }

    @Override
    public QueryResults<Export> list(String publicationId, int page, int pageSize) {
        return exportService.list(publicationId, page, pageSize);
    }

    @Override
    public FileRef getFile(String fileRefId) {
        return fileService.find(fileRefId);
    }

    private JobEventDto createJob(ExportRequestDto requestDto) {
        JobEventCreateDto createDto = new JobEventCreateDto();
        createDto.setPublicationId(requestDto.getPublicationId());
        createDto.setConfig(requestDto.getConfig());

        JobEventDto jobEvent = jobEventService.create(createDto);
        jobEventService.enqueueJob(jobEvent.getId());

        return jobEvent;
    }
}

package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        // Include index field, because it is used for naming export files;
        requestDto.getConfig().getParams().includeFields("index");

        if (requestDto.getConfig().getParams().getSorting().isEmpty()) {
            requestDto.getConfig().getParams().getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }

        JobEventCreateDto createDto = new JobEventCreateDto();
        createDto.setPublicationId(requestDto.getPublicationId());
        createDto.setConfig(requestDto.getConfig());

        JobEventDto jobEvent = jobEventService.create(createDto);
        jobEventService.enqueueJob(jobEvent.getId());

        return jobEvent;
    }
}

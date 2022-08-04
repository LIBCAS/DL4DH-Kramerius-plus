package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
public class ExportFacadeImpl implements ExportFacade {

    private final JobEventService jobEventService;

    private final ExportService exportService;

    private final FileService fileService;

    @Autowired
    public ExportFacadeImpl(JobEventService jobEventService, ExportService exportService, FileService fileService) {
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

    @Override
    public ExportDto findByJobEvent(String jobEventId) {
        notNull(jobEventId, () -> new ValidationException("Parameter jobEventId cannot be null", ValidationException.ErrorCode.INVALID_PARAMETERS));

        return exportService.findByJobEvent(jobEventId);
    }

    private JobEventDto createJob(ExportRequestDto requestDto) {
        validateParams(requestDto.getConfig().getParams());

        JobEventCreateDto createDto = new JobEventCreateDto();
        createDto.setPublicationId(requestDto.getPublicationId());
        createDto.setConfig(requestDto.getConfig());
        createDto.setJobName(requestDto.getName());

        JobEventDto jobEventDto = jobEventService.create(createDto);

        jobEventService.enqueueJob(jobEventDto);

        return jobEventDto;
    }

    private void validateParams(Params params) {
        if (!params.getIncludeFields().isEmpty()) {
            // Include index field, because it is used for naming export files;
            params.includeFields("index");
        }

        if (params.getSorting().isEmpty()) {
            params.getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }
    }
}

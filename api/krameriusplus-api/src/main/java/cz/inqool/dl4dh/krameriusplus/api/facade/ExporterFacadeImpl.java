package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.ExportJobConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.INVALID_EXPORT_TYPE;

@Component
public class ExporterFacadeImpl implements ExporterFacade {

    private final PublicationService publicationService;

    private final JobEventService jobEventService;

    private final ExporterService exporterService;

    private final FileService fileService;

    @Autowired
    public ExporterFacadeImpl(PublicationService publicationService, JobEventService jobEventService, ExporterService exporterService, FileService fileService) {
        this.publicationService = publicationService;
        this.jobEventService = jobEventService;
        this.exporterService = exporterService;
        this.fileService = fileService;
    }

    @Override
    public JobEventDto exportTei(String publicationId, TeiParams params) {
        return createJob(publicationId, ExportFormat.TEI, params);
    }

    @Override
    public JobEventDto export(String publicationId, String exportFormatStr, Params params) {
        ExportFormat exportFormat = getFormatFromString(exportFormatStr);

        return createJob(publicationId, exportFormat, params);
    }

    @Override
    public QueryResults<Export> list(String publicationId, int page, int pageSize) {
        return exporterService.list(publicationId, page, pageSize);
    }

    @Override
    public FileRef getFile(String fileRefId) {
        return fileService.find(fileRefId);
    }

    private JobEventDto createJob(String publicationId, ExportFormat exportFormat, Params params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new TeiParams();
        }

        JobEventCreateDto createDto = new JobEventCreateDto();
        createDto.setPublicationId(publicationId);

        ExportJobConfigDto config = new ExportJobConfigDto();
        config.setExportFormat(exportFormat);
        config.setParams(params);
        config.setPublicationTitle(publication.getTitle());

        createDto.setConfig(config);

        JobEventDto jobEvent = jobEventService.create(createDto);
        jobEventService.enqueueJob(jobEvent.getId());

        return jobEvent;
    }

    private ExportFormat getFormatFromString(String exportFormatStr) {
        try {
            ExportFormat exportFormat = ExportFormat.fromString(exportFormatStr);

            if (exportFormat == ExportFormat.TEI) {
                throw new IllegalArgumentException("For export in TEI format, use endpoint /api/export/{id}/tei, " +
                        "which can process extended TEI parameters.");
            }

            return exportFormat;
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage(), INVALID_EXPORT_TYPE, e);
        }
    }
}

package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.ExportingJobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("/api/export")
public class ExporterApi {

    private final ExporterService exporterService;
    private final FileService fileService;
    private final PublicationService publicationService;
    private final JmsProducer jmsProducer;
    private final JobEventService jobEventService;

    @Autowired
    public ExporterApi(ExporterService exporterService,
                       FileService fileService,
                       PublicationService publicationService,
                       JmsProducer jmsProducer, JobEventService jobEventService) {
        this.exporterService = exporterService;
        this.fileService = fileService;
        this.publicationService = publicationService;
        this.jmsProducer = jmsProducer;
        this.jobEventService = jobEventService;
    }

    @PostMapping("/{id}/tei")
    public JobEventDto exportTei(@PathVariable("id") String publicationId,
                              @RequestBody(required = false) TeiParams params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new TeiParams();
        }

        ExportingJobEventCreateDto createDto = new ExportingJobEventCreateDto();
        createDto.setPublicationId(publicationId);
        createDto.setExportFormat(ExportFormat.TEI);
        createDto.setParams(params);
        createDto.setPublicationTitle(publication.getTitle());

        return jobEventService.create(createDto);
    }

    @PostMapping("/{id}/{format}")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @PathVariable("format") String stringFormat,
                              @RequestBody(required = false) Params params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new Params();
        }

        ExportingJobEventCreateDto createDto = new ExportingJobEventCreateDto();
        createDto.setPublicationId(publicationId);
        createDto.setExportFormat(ExportFormat.fromString(stringFormat));
        createDto.setParams(params);
        createDto.setPublicationTitle(publication.getTitle());

        return jobEventService.create(createDto);
    }

    @GetMapping("/list")
    public List<Export> listExports() {
        return exporterService.list();
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") String fileRefId) {
        FileRef file = fileService.find(fileRefId);

        file.open();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"")
                .header("Content-Length", String.valueOf(file.getSize()))
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new InputStreamResource(file.getStream()));
    }
}

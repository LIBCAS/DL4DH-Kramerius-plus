package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.ExportingJobEventCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Validated
@Tag(name = "Export", description = "Exportování")
@RestController
@RequestMapping("/api/export")
public class ExporterApi {

    private final ExporterService exporterService;
    private final FileService fileService;
    private final PublicationService publicationService;
    private final JobEventService jobEventService;

    @Autowired
    public ExporterApi(ExporterService exporterService,
                       FileService fileService,
                       PublicationService publicationService,
                       JobEventService jobEventService) {
        this.exporterService = exporterService;
        this.fileService = fileService;
        this.publicationService = publicationService;
        this.jobEventService = jobEventService;
    }

    @Operation(summary = "Create and start a new job of type EXPORT for TEI format. Job is started asynchronously. " +
            "TEI format has it's own endpoint because it has it's own type of parameters.")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
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

        JobEventDto jobEvent = jobEventService.create(createDto);
        jobEventService.enqueueJob(jobEvent.getId());

        return jobEvent;
    }

    @Operation(summary = "Create and start a new job of type EXPORT for formats other then TEI. Job is started asynchronously. ")
    @ApiResponse(responseCode = "200", description = "Job successfully created")
    @PostMapping("/{id}/{format}")
    public JobEventDto export(@PathVariable("id") String publicationId,
                              @Schema(allowableValues = {"json", "csv", "tsv"}) @PathVariable("format") String stringFormat,
                              @RequestBody(required = false) Params params) {
        ExportFormat exportFormat = ExportFormat.fromString(stringFormat);
        if (exportFormat == ExportFormat.TEI) {
            throw new IllegalArgumentException("For export in TEI format, use endpoint /api/export/{id}/tei, " +
                    "which can process extended TEI parameters.");
        }

        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new Params();
        }

        ExportingJobEventCreateDto createDto = new ExportingJobEventCreateDto();
        createDto.setPublicationId(publicationId);
        createDto.setExportFormat(exportFormat);
        createDto.setParams(params);
        createDto.setPublicationTitle(publication.getTitle());

        JobEventDto jobEvent = jobEventService.create(createDto);
        jobEventService.enqueueJob(jobEvent.getId());

        return jobEvent;
    }

    @Operation(summary = "List all exports.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/list")
    public List<Export> listExports() {
        return exporterService.list();
    }

    @Operation(summary = "Download export.")
    @ApiResponse(responseCode = "200", description = "OK")
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

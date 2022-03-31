package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.jms.ExportMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
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

    @Autowired
    public ExporterApi(ExporterService exporterService,
                       FileService fileService,
                       PublicationService publicationService,
                       JmsProducer jmsProducer) {
        this.exporterService = exporterService;
        this.fileService = fileService;
        this.publicationService = publicationService;
        this.jmsProducer = jmsProducer;
    }

    @PostMapping("/{id}/{format}")
    public void export(@PathVariable("id") String publicationId,
                       @PathVariable("format") String stringFormat,
                       @RequestBody(required = false) Params params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new Params();
        }

        jmsProducer.sendExportMessage(
                ExportMessage.newInstance(publicationId,
                        publication.getTitle(),
                        params,
                        ExportFormat.fromString(stringFormat),
                        Date.from(Instant.now())));
    }

    @PostMapping("/old/{id}/json")
    public void exportJson(@PathVariable("id") String publicationId,
                             @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        exporterService.export(publicationId, params, ExportFormat.JSON);
    }

    @PostMapping("/old/{id}/tei")
    public void exportTei(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) TeiParams params) {
        if (params == null) {
            params = new TeiParams();
        }

        exporterService.export(publicationId, params, ExportFormat.TEI);
    }

    @PostMapping("/old/{id}/csv")
    public void exportCsv(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }
        exporterService.export(publicationId, params, ExportFormat.CSV);
    }

    @PostMapping("/old/{id}/tsv")
    public void exportTsv(@PathVariable("id") String publicationId,
                            @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }
        exporterService.export(publicationId, params, ExportFormat.TSV);
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

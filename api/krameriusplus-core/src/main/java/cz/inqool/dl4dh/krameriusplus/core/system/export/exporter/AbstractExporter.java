package cz.inqool.dl4dh.krameriusplus.core.system.export.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractExporter implements Exporter {

    protected final ObjectMapper objectMapper;
    protected final FileService fileService;
    protected final PublicationService publicationService;

    protected AbstractExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService) {
        this.objectMapper = objectMapper;
        this.fileService = fileService;
        this.publicationService = publicationService;
    }

    protected Export createExport(Publication publication, FileRef fileRef) {
        Export export = new Export();
        export.setPublication(publication);
        export.setFileRef(fileRef);

        return export;
    }
}

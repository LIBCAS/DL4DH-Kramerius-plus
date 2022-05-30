package cz.inqool.dl4dh.krameriusplus.service.system.export.exporter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TsvExporter extends SvExporter {

    @Autowired
    public TsvExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService, ExportFormat.TSV);
    }
}

package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.domain.dao.ExportFormat.TSV;

@Component
public class TsvExporter extends SvExporter {

    @Autowired
    public TsvExporter(ObjectMapper objectMapper, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService, TSV);
    }
}

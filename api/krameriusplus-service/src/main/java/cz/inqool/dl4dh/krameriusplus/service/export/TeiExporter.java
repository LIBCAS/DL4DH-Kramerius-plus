package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.service.export.ExportFormat.TEI;

@Component
public class TeiExporter extends AbstractExporter {

    private final TeiConnector teiConnector;

    @Autowired
    public TeiExporter(ObjectMapper objectMapper, TeiConnector teiConnector, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService);
        this.teiConnector = teiConnector;
    }

    @Override
    public FileRef export(String publicationId, Params params) {
        Publication publication = publicationService.findWithPages(publicationId, PublicationService.ALL_PAGES);

        if (!(publication instanceof PagesAware)) {
            throw new IllegalArgumentException("Tei cannot be generated from a publication which does not have pages");
        }

        PagesAware publicationWithPages = (PagesAware) publication;
        String content = getTei(publicationWithPages, params);

        return fileService.create(publication, content, TEI);
    }

    @Override
    public ExportFormat getFormat() {
        return TEI;
    }

    private String getTei(PagesAware publication, Params params) {
        return teiConnector.merge(publication.getTeiHeader(),
                publication.getPages().stream().map(Page::getTeiBody).collect(Collectors.toList()),
                params);
    }
}

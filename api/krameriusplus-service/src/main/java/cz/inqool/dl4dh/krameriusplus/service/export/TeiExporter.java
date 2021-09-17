package cz.inqool.dl4dh.krameriusplus.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.export.filter.Params;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.stream.Collectors;

@Component
public class TeiExporter extends AbstractExporter {

    private final TeiConnector teiConnector;

    @Autowired
    public TeiExporter(ObjectMapper objectMapper, TeiConnector teiConnector) {
        super(objectMapper);
        this.teiConnector = teiConnector;
    }

    @Override
    public File export(Publication publication, Params params) {
        if (!(publication instanceof PagesAware)) {
            throw new IllegalArgumentException("Publication does not have pages");
        }

        File file = createFile(publication.getId());

        PagesAware publicationWithPages = (PagesAware) publication;
        writeToFile(getTei(publicationWithPages, params), file);

        return file;
    }

    private String getTei(PagesAware publication, Params params) {
        return teiConnector.merge(publication.getTeiHeader(),
                publication.getPages().stream().map(Page::getTeiBody).collect(Collectors.toList()),
                params);
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.TEI;
    }
}

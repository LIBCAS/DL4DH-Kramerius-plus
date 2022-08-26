package cz.inqool.dl4dh.krameriusplus.service.system.exporter;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class TeiExporter {

    private final PublicationService publicationService;

    private final FileService fileService;

    private final TeiConnector teiConnector;

    @Autowired
    public TeiExporter(PublicationService publicationService, FileService fileService, TeiConnector teiConnector) {
        this.publicationService = publicationService;
        this.fileService = fileService;
        this.teiConnector = teiConnector;
    }

    public void export(String publicationId, TeiExportParams teiParams, Path teiFile) throws IOException {
        Publication publication = publicationService.find(publicationId);
        FileRef teiHeader = fileService.find(publication.getTeiHeaderFileId());

        QueryResults<Page> pages = publicationService.findAllPages(publicationId);

        List<InputStream> teiBodies = new ArrayList<>();

        try (InputStream teiHeaderIs = teiHeader.open()) {
            for (Page page : pages.getResults()) {
                FileRef teiBody = fileService.find(page.getTeiBodyFileId());
                teiBodies.add(teiBody.open());
            }

            teiConnector.merge(teiHeaderIs, teiBodies, teiParams, teiFile);
        } finally {
            for (InputStream teiBodyStream : teiBodies) {
                teiBodyStream.close();
            }
        }
    }
}

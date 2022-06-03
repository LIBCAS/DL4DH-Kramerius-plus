package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
@StepScope
@Slf4j
public class EnrichPublicationTeiProcessor implements ItemProcessor<Publication, Publication> {

    private final TeiConnector teiConnector;

    private final FileService fileService;

    @Autowired
    public EnrichPublicationTeiProcessor(TeiConnector teiConnector, FileService fileService) {
        this.teiConnector = teiConnector;
        this.fileService = fileService;
    }

    @Override
    public Publication process(Publication item) throws Exception {
        String teiHeader = teiConnector.convertToTeiHeader(item);

        byte[] teiHeaderBytes = teiHeader.getBytes(StandardCharsets.UTF_8);

        try (ByteArrayInputStream is = new ByteArrayInputStream(teiHeaderBytes)) {
            log.debug("Saving TEI HEADER to a file for publication {}", item.getId());

            FileRef fileRef = fileService.create(is,
                    teiHeaderBytes.length,
                    item.getId() + "_tei_header.xml",
                    ContentType.APPLICATION_XML.getMimeType());

            item.setTeiHeaderFileId(fileRef.getId());
        }

        return item;
    }
}

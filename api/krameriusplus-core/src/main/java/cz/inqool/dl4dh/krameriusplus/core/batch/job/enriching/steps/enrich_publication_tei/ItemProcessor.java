package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_publication_tei;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Named(value = Steps.EnrichPublicationTei.PROCESSOR_NAME)
@StepScope
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Publication, Publication> {

    private final TeiConnector teiConnector;

    private final FileService fileService;

    @Autowired
    ItemProcessor(TeiConnector teiConnector, FileService fileService) {
        this.teiConnector = teiConnector;
        this.fileService = fileService;
    }

    @Override
    public Publication process(@NonNull Publication publication) throws Exception {
        String teiHeader = teiConnector.convertToTeiHeader(publication);

        byte[] teiHeaderBytes = teiHeader.getBytes(StandardCharsets.UTF_8);

        try (ByteArrayInputStream is = new ByteArrayInputStream(teiHeaderBytes)) {
            log.debug("Saving TEI HEADER to a file for publication {}", publication.getId());

            FileRef fileRef = fileService.create(is,
                    teiHeaderBytes.length,
                    publication.getId() + "_tei_header.xml",
                    ContentType.APPLICATION_XML.getMimeType());

            publication.setTeiHeaderFileId(fileRef.getId());
        }

        return publication;
    }
}

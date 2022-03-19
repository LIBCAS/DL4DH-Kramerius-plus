package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages_tei;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
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

@StepScope
@Named(Steps.EnrichPagesTei.PROCESSOR_NAME)
@Slf4j
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    private final TeiConnector teiConnector;

    private final FileService fileService;

    @Autowired
    ItemProcessor(TeiConnector teiConnector, FileService fileService) {
        this.teiConnector = teiConnector;
        this.fileService = fileService;
    }

    @Override
    public Page process(@NonNull Page page) throws Exception {
        String teiPage = teiConnector.convertToTeiPage(page);

        byte[] teiPageBytes = teiPage.getBytes(StandardCharsets.UTF_8);

        try (ByteArrayInputStream is = new ByteArrayInputStream(teiPageBytes)) {
            log.debug("Saving TEI page to a file for page {}", page.getId());

            FileRef fileRef = fileService.create(
                    is,
                    teiPageBytes.length,
                    page.getId() + "_tei_page.xml",
                    ContentType.APPLICATION_XML.getMimeType());

            page.setTeiBodyFileId(fileRef.getId());
        }

        return page;
    }
}

package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
@StepScope
@Slf4j
public class EnrichPagesTeiProcessor implements ItemProcessor<Page, Page> {

    private final TeiConnector teiConnector;

    private final FileService fileService;

    @Autowired
    public EnrichPagesTeiProcessor(TeiConnector teiConnector, FileService fileService) {
        this.teiConnector = teiConnector;
        this.fileService = fileService;
    }

    @Override
    public Page process(@NonNull Page item) throws Exception {
        String teiPage = teiConnector.convertToTeiPage(item);

        byte[] teiPageBytes = teiPage.getBytes(StandardCharsets.UTF_8);

        try (ByteArrayInputStream is = new ByteArrayInputStream(teiPageBytes)) {
            log.trace("Saving TEI page to a file for page {}", item.getId());

            FileRef fileRef = fileService.create(
                    is,
                    teiPageBytes.length,
                    item.getId() + "_tei_page.xml",
                    ContentType.APPLICATION_XML.getMimeType());

            item.setTeiBodyFileId(fileRef.getId());
        }

        return item;
    }
}

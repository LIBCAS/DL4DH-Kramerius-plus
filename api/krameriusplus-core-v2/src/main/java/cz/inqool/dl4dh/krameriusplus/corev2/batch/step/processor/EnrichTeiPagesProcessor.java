package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.tei.TeiMessenger;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileService;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Component
@StepScope
public class EnrichTeiPagesProcessor implements ItemProcessor<Page, Page> {

    private TeiMessenger teiMessenger;

    private FileService fileService;

    @Override
    public Page process(Page item) throws Exception {
        String tei = teiMessenger.convertPage(item);

        byte[] teiBytes = tei.getBytes(StandardCharsets.UTF_8);

        try (InputStream is = new ByteArrayInputStream(teiBytes)) {
            FileRef fileRef = fileService.create(is, teiBytes.length, item.getId(), ContentType.APPLICATION_XML.getMimeType());
            item.setTeiBodyFileId(fileRef.getId());

            return item;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Autowired
    public void setTeiMessenger(TeiMessenger teiMessenger) {
        this.teiMessenger = teiMessenger;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Component
public class TeiEnricher {

    private FileService fileService;

    private TeiMessenger teiMessenger;

    private PageStore pageStore;

    private PublicationStore publicationStore;

    public void enrichPage(String pageId) {
        Page page = pageStore.findById(pageId).orElseThrow(() -> new MissingObjectException(Page.class, pageId));
        String tei = teiMessenger.convertPage(page);

        byte[] teiBytes = tei.getBytes(StandardCharsets.UTF_8);

        try (InputStream is = new ByteArrayInputStream(teiBytes)) {
            FileRef fileRef = fileService.create(is, teiBytes.length, page.getId(), ContentType.APPLICATION_XML.getMimeType());
            page.setTeiBodyFileId(fileRef.getId());

            pageStore.save(page);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void enrichPublication(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        String tei = teiMessenger.convertHeader(publication);
        byte[] teiBytes = tei.getBytes(StandardCharsets.UTF_8);

        try (InputStream is = new ByteArrayInputStream(teiBytes)) {
            FileRef fileRef = fileService.create(is, teiBytes.length, publication.getId(), ContentType.APPLICATION_XML.getMimeType());
            publication.setTeiHeaderFileId(fileRef.getId());

            publicationStore.save(publication);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setTeiMessenger(TeiMessenger teiMessenger) {
        this.teiMessenger = teiMessenger;
    }

    @Autowired
    public void setPageStore(PageStore pageStore) {
        this.pageStore = pageStore;
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }
}

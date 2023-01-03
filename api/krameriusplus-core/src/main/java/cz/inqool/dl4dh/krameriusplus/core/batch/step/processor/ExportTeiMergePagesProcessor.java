package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.TeiEnrichmentException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.enricher.tei.TeiMessenger;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import static cz.inqool.dl4dh.krameriusplus.api.exception.TeiEnrichmentException.ErrorCode.MISSING_TEI_BODY_FILE;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.TEI_SESSION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
public class ExportTeiMergePagesProcessor implements ItemProcessor<Page, Page> {

    private TeiMessenger teiMessenger;

    private FileService fileService;

    @Value("#{jobExecutionContext['" + TEI_SESSION_ID + "']}")
    private String sessionId;

    @Override
    public Page process(Page item) throws Exception {
        notNull(item.getTeiBodyFileId(), () -> new TeiEnrichmentException("Page " + item.getId() + " has no teiBodyFileId.", MISSING_TEI_BODY_FILE));

        FileRef fileRef = fileService.find(item.getTeiBodyFileId());
        notNull(fileRef, () -> new TeiEnrichmentException("TEI body file " + item.getTeiBodyFileId() + " for page: " + item.getId() + " not found.", MISSING_TEI_BODY_FILE));

        try (InputStream is = fileRef.open()) {
            teiMessenger.addMerge(sessionId, is);
        }

        return item;
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

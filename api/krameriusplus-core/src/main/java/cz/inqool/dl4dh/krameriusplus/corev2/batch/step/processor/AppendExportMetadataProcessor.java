package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.PublicationProvider;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class AppendExportMetadataProcessor implements ItemProcessor<Page, Page> {

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    private Path exportDirectory;

    private PublicationProvider publicationProvider;

    @Override
    public Page process(Page item) throws Exception {
        return null;
    }

    @Autowired
    public void setExportDirectory(@Value("#{executionContext['" + DIRECTORY + "']}") String exportDirectory) {
        this.exportDirectory = Path.of(exportDirectory);
    }

    @Autowired
    public void setPublicationProvider(PublicationProvider publicationProvider) {
        this.publicationProvider = publicationProvider;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.DigitalObjectProvider;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class AppendExportMetadataProcessor implements ItemProcessor<Page, Page> {

    @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}")
    private String publicationId;

    private Path exportDirectory;

    private DigitalObjectProvider digitalObjectProvider;

    @Override
    public Page process(Page item) throws Exception {
        return null;
    }

    @Autowired
    public void setExportDirectory(@Value("#{executionContext['" + DIRECTORY + "']}") String exportDirectory) {
        this.exportDirectory = Path.of(exportDirectory);
    }

    @Autowired
    public void setPublicationProvider(DigitalObjectProvider digitalObjectProvider) {
        this.digitalObjectProvider = digitalObjectProvider;
    }
}

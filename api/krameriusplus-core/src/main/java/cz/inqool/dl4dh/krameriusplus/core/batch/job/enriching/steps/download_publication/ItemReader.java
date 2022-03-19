package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.download_publication;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Component
@Named(Steps.DownloadPublicationStep.READER_NAME)
class ItemReader implements org.springframework.batch.item.ItemReader<String> {

    private String publicationId;

    private boolean isRead = false;

    @Override
    public String read() {
        if (isRead) {
            return null;
        }

        isRead = true;
        return publicationId;
    }

    @Value("#{jobParameters['publicationId']}")
    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }
}

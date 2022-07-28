package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DownloadPublicationProcessor implements ItemProcessor<DigitalObject, Publication> {
    @Override
    public Publication process(@NonNull DigitalObject digitalObject) throws Exception {
        if (!(digitalObject instanceof Publication)) {
            throw new JobException("Received DigitalObject which is not a publication",
                    JobException.ErrorCode.UNSUPPORTED_DIGITAL_OBJECT);
        }

        Publication publication = (Publication) digitalObject;
        publication.setPublishInfo(new PublishInfo());

        if (publication.isPdf()) {
            throw new JobException("Received an epub or pdf",
                    JobException.ErrorCode.UNSUPPORTED_DIGITAL_OBJECT);
        }

        return publication;
    }
}

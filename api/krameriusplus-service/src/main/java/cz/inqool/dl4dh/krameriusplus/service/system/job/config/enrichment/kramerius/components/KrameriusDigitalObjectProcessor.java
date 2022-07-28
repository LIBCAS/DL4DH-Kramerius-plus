package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import lombok.NonNull;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class KrameriusDigitalObjectProcessor implements ItemProcessor<DigitalObject, Publication> {
    @Override
    public Publication process(@NonNull DigitalObject digitalObject) throws Exception {
        if (!(digitalObject instanceof Publication)) {
            throw new JobParametersInvalidException("Received DigitalObject which is not a publication");
        }

        Publication publication = (Publication) digitalObject;
        publication.setPublishInfo(new PublishInfo());

        if (publication.isPdf()) {
            throw new JobParametersInvalidException("Received an epub or pdf");
        }

        return publication;
    }
}

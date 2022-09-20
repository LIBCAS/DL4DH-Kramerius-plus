package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;


import cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException.ErrorCode.UNSUPPORTED_DIGITAL_OBJECT;

@Component
@StepScope
public class DownloadDigitalObjectsProcessor implements ItemProcessor<DigitalObject, DigitalObject> {

    @Override
    public DigitalObject process(@NonNull DigitalObject item) {
        if (item instanceof Publication) {
            Publication publication = (Publication) item;
            publication.setPublishInfo(new PublishInfo());

            if (publication.isPdf()) {
                throw new EnrichingException("Received a Publication in EPUB or PDF format", UNSUPPORTED_DIGITAL_OBJECT);
            }
        }

        return item;
    }
}

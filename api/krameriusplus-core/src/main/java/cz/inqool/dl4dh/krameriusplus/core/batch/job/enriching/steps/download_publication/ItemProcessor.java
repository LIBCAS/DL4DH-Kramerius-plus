package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.download_publication;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.List;

@StepScope
@Slf4j
@Named(Steps.DownloadPublicationStep.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<String, Publication> {

    private final DataProvider dataProvider;

    private final JmsProducer jmsProducer;

    @Autowired
    ItemProcessor(DataProvider dataProvider, JmsProducer jmsProducer) {
        this.dataProvider = dataProvider;
        this.jmsProducer = jmsProducer;
    }

    @Override
    public Publication process(@NonNull String publicationId) {
        log.debug("Downloading publication with ID={}", publicationId);

        DigitalObject digitalObject = dataProvider.getDigitalObject(publicationId);
        if (!(digitalObject instanceof Publication)) {
            throw new IllegalStateException("Received DigitalObject which is not a publication");
        }

        Publication publication = (Publication) digitalObject;

        log.debug("Downloading pages for publication with ID={}", publicationId);

        List<DigitalObject> childObjects = dataProvider.getDigitalObjectsForParent(publication);

        // assign only pages
        for (DigitalObject child : childObjects) {
            if (child instanceof Page) {
                publication.getPages().add((Page) child);
            } else if (child instanceof Publication) {
                jmsProducer.sendEnrichMessage(child.getId());
            }
        }

        return publication;
    }
}

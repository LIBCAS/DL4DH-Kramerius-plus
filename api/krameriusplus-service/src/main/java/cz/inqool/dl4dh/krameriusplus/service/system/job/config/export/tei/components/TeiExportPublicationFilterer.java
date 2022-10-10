package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Filter out Digital objects which are Publications and have Pages
 *
 * @author Filip Kollar
 */
@Component
@StepScope
public class TeiExportPublicationFilterer implements ItemProcessor<DigitalObject, Publication> {

    @Override
    public Publication process(DigitalObject item) throws Exception {
        if (item instanceof Publication) {
            return ((Publication) item).getPageCount() > 0 ? (Publication) item : null;
        }
        return null;
    }
}

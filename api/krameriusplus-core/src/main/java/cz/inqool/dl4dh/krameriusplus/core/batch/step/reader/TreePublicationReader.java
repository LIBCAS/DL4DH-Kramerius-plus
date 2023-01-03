package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * Reader that reads the whole publication tree for given root ID
 */
@Component
@StepScope
public class TreePublicationReader implements ItemReader<Publication> {

    @Override
    public Publication read() {
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }
}

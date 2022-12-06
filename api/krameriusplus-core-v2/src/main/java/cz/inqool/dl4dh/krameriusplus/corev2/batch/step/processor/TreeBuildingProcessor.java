package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class TreeBuildingProcessor implements ItemProcessor<String, Publication> {

    private final KrameriusMessenger krameriusMessenger;

    public TreeBuildingProcessor(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }


    @Override
    public Publication process(String item) {
        DigitalObject root = krameriusMessenger.getDigitalObject(item);

        if (!(root instanceof Publication)) {
            throw new IllegalStateException(String.format("Item with uuid:%s is a %s", item, root.getClass().getSimpleName()));
        }


        Publication publication = ((Publication) root);
        return fetchTree(publication);
    }

    private Publication fetchTree(Publication publication) {
        List<DigitalObject> publicationChildren = krameriusMessenger
                .getDigitalObjectsForParent(publication.getId());

        publication.setChildren(publicationChildren);

        List<Publication> publications = publicationChildren.stream()
                .filter(digitalObject -> digitalObject instanceof Publication)
                .map(digitalObject -> ((Publication) digitalObject))
                .collect(Collectors.toList());

        publications.forEach(this::fetchTree);

        return publication;
    }
}

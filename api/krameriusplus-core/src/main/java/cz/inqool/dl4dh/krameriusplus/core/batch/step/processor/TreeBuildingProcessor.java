package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class TreeBuildingProcessor implements ItemProcessor<String, Publication> {

    private final KrameriusMessenger krameriusMessenger;

    @Autowired
    public TreeBuildingProcessor(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }

    @Override
    public Publication process(String item) {
        DigitalObject root = krameriusMessenger.getDigitalObject(item);

        if (!(root instanceof Publication)) {
            throw new IllegalStateException(String.format("Item with uuid:%s is a %s, expected DigitalObject of type Publication",
                    item, root.getClass().getSimpleName()));
        }

        Publication publication = ((Publication) root);
        publication.setIsRootEnrichment(true);

        return fetchTree(publication);
    }

    private Publication fetchTree(Publication publication) {
        List<DigitalObject> publicationChildren = krameriusMessenger
                .getDigitalObjectsForParent(publication.getId());

        List<Publication> publications = filterObjects(publicationChildren, Publication.class);
        List<Page> pages = filterObjects(publicationChildren, Page.class);
        publication.setChildren(publications);
        publication.setPages(pages);

        publications.forEach(this::fetchTree);

        return publication;
    }


    private <T extends DigitalObject>List<T> filterObjects(List<DigitalObject> objects, Class<T> targetClass) {
        return objects.stream()
                .filter(targetClass::isInstance)
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }
}

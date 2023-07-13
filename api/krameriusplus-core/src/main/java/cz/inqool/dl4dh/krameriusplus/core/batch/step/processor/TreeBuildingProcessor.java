package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.DigitalObjectProvider;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class TreeBuildingProcessor implements ItemProcessor<String, Publication> {

    private final DigitalObjectProvider digitalObjectProvider;

    @Autowired
    public TreeBuildingProcessor(DigitalObjectProvider digitalObjectProvider) {
        this.digitalObjectProvider = digitalObjectProvider;
    }

    @Override
    public Publication process(String item) {
        Publication root = digitalObjectProvider.find(item);
        root.setIsRootEnrichment(true);

        return fetchTree(root);
    }

    private Publication fetchTree(Publication publication) {
        List<? extends DigitalObject> publicationChildren = digitalObjectProvider.findChildren(publication.getId());
        List<Publication> publications = filterObjects(publicationChildren, Publication.class);
        List<Page> pages = filterObjects(publicationChildren, Page.class);
        publication.setChildren(publications);
        publication.setPages(pages);
        publication.setPageCount(pages.size());

        publications.forEach(this::fetchTree);

        return publication;
    }


    private <T extends DigitalObject> List<T> filterObjects(List<? extends DigitalObject> objects, Class<T> targetClass) {
        return objects.stream()
                .filter(targetClass::isInstance)
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }
}

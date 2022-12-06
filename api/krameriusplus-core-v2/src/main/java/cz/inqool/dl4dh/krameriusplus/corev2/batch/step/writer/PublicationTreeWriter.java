package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class PublicationTreeWriter implements ItemWriter<Publication> {

    private final PageStore pageStore;

    private final PublicationStore publicationStore;

    @Autowired
    public PublicationTreeWriter(PageStore pageStore, PublicationStore publicationStore) {
        this.pageStore = pageStore;
        this.publicationStore = publicationStore;
    }


    @Override
    public void write(List<? extends Publication> items) {
        items.forEach(this::writeTree);
    }

    private void writeTree(Publication publication) {
        if (publication == null) {
            return;
        }

        publicationStore.save(publication);
        pageStore.saveAll(publication.getPages());
        publication.getChildren().forEach(this::writeTree);
    }
}

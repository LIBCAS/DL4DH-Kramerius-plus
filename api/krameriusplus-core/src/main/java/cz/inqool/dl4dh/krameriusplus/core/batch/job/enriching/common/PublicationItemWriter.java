package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class PublicationItemWriter implements ItemWriter<Publication> {

    private final PublicationService publicationService;

    @Autowired
    public PublicationItemWriter(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Override
    public void write(List<? extends Publication> items) {
        if (items.size() != 1) {
            throw new IllegalArgumentException("Items must contain exactly 1 publication");
        }

        publicationService.save(items.get(0));
    }
}

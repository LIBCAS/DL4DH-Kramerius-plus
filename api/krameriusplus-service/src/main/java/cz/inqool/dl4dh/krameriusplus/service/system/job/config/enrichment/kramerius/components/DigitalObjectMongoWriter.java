package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PageMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PublicationMongoWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@StepScope
@Component
public class DigitalObjectMongoWriter implements ItemWriter<DigitalObject> {

    private final PageMongoWriter pageMongoWriter;

    private final PublicationMongoWriter publicationMongoWriter;

    @Autowired
    public DigitalObjectMongoWriter(PageMongoWriter pageMongoWriter, PublicationMongoWriter publicationMongoWriter) {
        this.pageMongoWriter = pageMongoWriter;
        this.publicationMongoWriter = publicationMongoWriter;
    }


    @Override
    public void write(List<? extends DigitalObject> items) throws Exception {
        pageMongoWriter.write(items.stream().filter(item -> item instanceof Page)
                .map(item -> (Page) item)
                .collect(Collectors.toList()));

        publicationMongoWriter.write(items.stream().filter(item -> item instanceof Publication)
                .map(item -> (Publication) item)
                .collect(Collectors.toList()));
    }
}

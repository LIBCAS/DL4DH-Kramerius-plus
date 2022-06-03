package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PersistentStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PublicationMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.KrameriusPublicationReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobStep.DOWNLOAD_PUBLICATION;

@Component
@Slf4j
public class DownloadPublicationStepFactory extends PersistentStepFactory<Publication, Publication> {

    private final KrameriusPublicationReader reader;

    private final PublicationMongoWriter writer;

    @Autowired
    public DownloadPublicationStepFactory(KrameriusPublicationReader reader, PublicationMongoWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return DOWNLOAD_PUBLICATION;
    }

    @Override
    protected ItemReader<Publication> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }
}

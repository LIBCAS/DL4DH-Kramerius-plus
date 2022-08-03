package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.mods;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PublicationMongoWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PUBLICATION_MODS;


@Component
public class EnrichPublicationModsStepFactory extends FlowStepFactory<Publication, Publication> {
    private EnrichPublicationModsProcessor processor;

    private PublicationMongoReader reader;

    private PublicationMongoWriter writer;

    @Override
    protected ItemReader<Publication> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<Publication, Publication> getItemProcessor() {
        return processor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PUBLICATION_MODS;
    }

    @Autowired
    public void setProcessor(EnrichPublicationModsProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setReader(PublicationMongoReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setWriter(PublicationMongoWriter writer) {
        this.writer = writer;
    }
}

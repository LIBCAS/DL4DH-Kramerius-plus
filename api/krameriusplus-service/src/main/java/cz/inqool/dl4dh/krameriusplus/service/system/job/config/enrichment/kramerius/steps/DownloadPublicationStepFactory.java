package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PublicationMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.KrameriusDigitalObjectProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.KrameriusPublicationReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PUBLICATION;

@Component
@Slf4j
public class DownloadPublicationStepFactory extends FlowStepFactory<DigitalObject, Publication> {

    private final KrameriusPublicationReader reader;

    private final PublicationMongoWriter writer;

    private final KrameriusDigitalObjectProcessor processor;

    @Autowired
    public DownloadPublicationStepFactory(KrameriusPublicationReader reader, PublicationMongoWriter writer, KrameriusDigitalObjectProcessor processor) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return DOWNLOAD_PUBLICATION;
    }

    @Override
    protected ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<DigitalObject, Publication> getItemProcessor() {
        return processor;
    }
}

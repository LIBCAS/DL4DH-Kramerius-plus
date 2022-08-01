package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PageMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadPublicationChildrenProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadPublicationChildrenReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PUBLICATION_CHILDREN;

@Slf4j
@Component
public class DownloadPublicationChildrenStepFactory extends FlowStepFactory<DigitalObject, Page> {

    private final DownloadPublicationChildrenReader reader;

    private final DownloadPublicationChildrenProcessor processor;

    private final PageMongoWriter writer;

    @Autowired
    public DownloadPublicationChildrenStepFactory(DownloadPublicationChildrenReader reader,
                                                  DownloadPublicationChildrenProcessor processor,
                                                  PageMongoWriter writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return DOWNLOAD_PUBLICATION_CHILDREN;
    }

    @Override
    protected ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    @Override
    protected ItemProcessor<DigitalObject, Page> getItemProcessor() {
        return processor;
    }

    @Override
    protected ItemWriter<Page> getItemWriter() {
        return writer;
    }
}

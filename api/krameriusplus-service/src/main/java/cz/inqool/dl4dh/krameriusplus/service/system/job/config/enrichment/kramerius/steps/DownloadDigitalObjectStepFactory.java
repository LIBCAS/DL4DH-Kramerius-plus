package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.DigitalObjectMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadDigitalObjectProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_DIGITAL_OBJECTS;

@Component
public class DownloadDigitalObjectStepFactory extends FlowStepFactory<DigitalObject, DigitalObject> {

    private DownloadDigitalObjectReader itemReader;

    private DownloadDigitalObjectProcessor itemProcessor;

    private DigitalObjectMongoWriter itemWriter;

    @Override
    protected String getStepName() {
        return DOWNLOAD_DIGITAL_OBJECTS;
    }

    @Override
    public DownloadDigitalObjectReader getItemReader() {
        return itemReader;
    }

    @Override
    public DigitalObjectMongoWriter getItemWriter() {
        return itemWriter;
    }

    @Override
    protected ItemProcessor<DigitalObject, DigitalObject> getItemProcessor() {
        return itemProcessor;
    }

    @Autowired
    public void setItemReader(DownloadDigitalObjectReader itemReader) {
        this.itemReader = itemReader;
    }

    @Autowired
    public void setItemWriter(DigitalObjectMongoWriter itemWriter) {
        this.itemWriter = itemWriter;
    }

    @Autowired
    public void setItemProcessor(DownloadDigitalObjectProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
    }
}
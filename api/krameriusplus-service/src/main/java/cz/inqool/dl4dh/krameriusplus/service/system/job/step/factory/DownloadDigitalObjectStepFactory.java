package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.DownloadDigitalObjectProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.DigitalObjectMongoWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.DOWNLOAD_DIGITAL_OBJECTS;

@Configuration
public class DownloadDigitalObjectStepFactory extends FlowStepFactory<DigitalObject, DigitalObject> {

    private DownloadDigitalObjectReader itemReader;

    private DownloadDigitalObjectProcessor itemProcessor;

    private DigitalObjectMongoWriter itemWriter;

    @Bean(DOWNLOAD_DIGITAL_OBJECTS)
    @Override
    public Step build() {
        return super.build();
    }

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

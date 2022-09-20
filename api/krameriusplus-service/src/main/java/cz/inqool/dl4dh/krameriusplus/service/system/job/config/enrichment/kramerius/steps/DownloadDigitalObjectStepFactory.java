package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DigitalObjectMongoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadDigitalObjectsProcessor;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_DIGITAL_OBJECTS;


@Component
public class DownloadDigitalObjectStepFactory extends FlowStepFactory<DigitalObject, DigitalObject>{

    @Getter
    private final DownloadDigitalObjectReader itemReader;

    @Getter
    private final DigitalObjectMongoWriter itemWriter;

    private final DownloadDigitalObjectsProcessor itemProcessor;

    @Autowired
    public DownloadDigitalObjectStepFactory(DownloadDigitalObjectReader itemReader, DigitalObjectMongoWriter itemWriter, DownloadDigitalObjectsProcessor itemProcessor) {
        this.itemReader = itemReader;
        this.itemWriter = itemWriter;
        this.itemProcessor = itemProcessor;
    }

    @Override
    protected ItemProcessor<DigitalObject, DigitalObject> getItemProcessor() {
        return itemProcessor;
    }

    @Override
    protected String getStepName() {
        return DOWNLOAD_DIGITAL_OBJECTS;
    }
}

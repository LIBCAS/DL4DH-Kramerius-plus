package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.ParadataStepListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.DownloadPagesAltoProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PAGES_ALTO;

@Component
@Slf4j
public class DownloadPagesAltoStepFactory extends PageMongoFlowStepFactory {

    private final DownloadPagesAltoProcessor processor;

    private final ParadataStepListener stepListener;

    @Autowired
    public DownloadPagesAltoStepFactory(DownloadPagesAltoProcessor processor, ParadataStepListener stepListener) {
        this.processor = processor;
        this.stepListener = stepListener;
    }

    @Override
    protected String getStepName() {
        return DOWNLOAD_PAGES_ALTO;
    }

    @Override
    protected int getChunkSize() {
        return 10;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return processor;
    }

    @Override
    protected List<StepExecutionListener> getStepExecutionListeners() {
        return List.of(stepListener);
    }
}

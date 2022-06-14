package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.DownloadPagesAltoProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PAGES_ALTO;

@Component
@Slf4j
public class DownloadPagesAltoStepFactory extends PageMongoFlowStepFactory {

    private final DownloadPagesAltoProcessor processor;

    @Autowired
    public DownloadPagesAltoStepFactory(DownloadPagesAltoProcessor processor) {
        this.processor = processor;
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
}

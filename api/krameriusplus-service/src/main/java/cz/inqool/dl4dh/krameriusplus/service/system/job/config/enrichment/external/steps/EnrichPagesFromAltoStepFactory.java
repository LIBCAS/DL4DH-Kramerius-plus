package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.ParadataStepListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.DownloadPagesAltoProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesAltoItemProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesNameTagProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.EnrichPagesUDPipeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_FROM_ALTO;

@Component
@Slf4j
public class EnrichPagesFromAltoStepFactory extends PageMongoFlowStepFactory {

    private final DownloadPagesAltoProcessor downloadPagesAltoProcessor;

    private final EnrichPagesUDPipeProcessor enrichPagesUDPipeProcessor;

    private final EnrichPagesNameTagProcessor enrichPagesNameTagProcessor;

    private final EnrichPagesAltoItemProcessor enrichPagesAltoItemProcessor;

    private final ParadataStepListener paradataStepListener;

    @Autowired
    public EnrichPagesFromAltoStepFactory(DownloadPagesAltoProcessor downloadPagesAltoProcessor, EnrichPagesUDPipeProcessor enrichPagesUDPipeProcessor,
                                          EnrichPagesNameTagProcessor enrichPagesNameTagProcessor, EnrichPagesAltoItemProcessor enrichPagesAltoItemProcessor,
                                          ParadataStepListener paradataStepListener) {
        this.downloadPagesAltoProcessor = downloadPagesAltoProcessor;
        this.enrichPagesUDPipeProcessor = enrichPagesUDPipeProcessor;
        this.enrichPagesNameTagProcessor = enrichPagesNameTagProcessor;
        this.enrichPagesAltoItemProcessor = enrichPagesAltoItemProcessor;
        this.paradataStepListener = paradataStepListener;
    }

    @Override
    protected ItemProcessor<Page, Page> getItemProcessor() {
        return new CompositeItemProcessorBuilder<Page, Page>()
                .delegates(downloadPagesAltoProcessor,
                        enrichPagesUDPipeProcessor,
                        enrichPagesNameTagProcessor,
                        enrichPagesAltoItemProcessor).build();
    }

    @Override
    protected List<StepExecutionListener> getStepExecutionListeners() {
        return List.of(paradataStepListener, downloadPagesAltoProcessor, enrichPagesUDPipeProcessor, enrichPagesNameTagProcessor);
    }

    @Override
    protected int getChunkSize() {
        return 10;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_FROM_ALTO;
    }
}

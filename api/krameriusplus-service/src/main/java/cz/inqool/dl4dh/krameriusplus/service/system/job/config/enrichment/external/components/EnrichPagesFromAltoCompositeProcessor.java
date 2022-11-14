package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.FaultTolerantProcessor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class EnrichPagesFromAltoCompositeProcessor extends FaultTolerantProcessor<Page, Page> {

    private final CompositeItemProcessor<Page, Page> delegate;

    @Autowired
    public EnrichPagesFromAltoCompositeProcessor(DownloadPagesAltoProcessor downloadPagesAltoProcessor,
                                                 EnrichPagesUDPipeProcessor enrichPagesUDPipeProcessor,
                                                 EnrichPagesNameTagProcessor enrichPagesNameTagProcessor,
                                                 EnrichPagesAltoItemProcessor enrichPagesAltoItemProcessor) {
        delegate = new CompositeItemProcessor<>();
        delegate.setDelegates(List.of(downloadPagesAltoProcessor,
                enrichPagesUDPipeProcessor,
                enrichPagesNameTagProcessor, enrichPagesAltoItemProcessor));

    }

    @Override
    protected Page doProcess(Page item) throws Exception {
        return delegate.process(item);
    }
}


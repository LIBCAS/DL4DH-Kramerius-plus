package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.FaultTolerantProcessor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class EnrichPagesNdkCompositeProcessor extends FaultTolerantProcessor<Page, Page> {

    private final CompositeItemProcessor<Page, Page> delegate;

    @Autowired
    public EnrichPagesNdkCompositeProcessor(PreparePagesNdkProcessor preparePagesNdkProcessor,
                                            EnrichPagesNdkProcessor enrichPagesNdkProcessor) {
        delegate = new CompositeItemProcessor<>();
        delegate.setDelegates(List.of(preparePagesNdkProcessor,
                enrichPagesNdkProcessor));
    }

    @Override
    protected Page doProcess(Page item) throws Exception {
        return delegate.process(item);
    }
}

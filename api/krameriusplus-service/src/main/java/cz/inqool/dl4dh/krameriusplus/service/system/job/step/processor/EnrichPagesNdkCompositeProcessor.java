package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class EnrichPagesNdkCompositeProcessor implements ItemProcessor<Page, Page> {

    private final CompositeItemProcessor<Page, Page> delegate;

    @Autowired
    public EnrichPagesNdkCompositeProcessor(PreparePagesNdkProcessor preparePagesNdkProcessor,
                                            EnrichPagesNdkProcessor enrichPagesNdkProcessor) {
        delegate = new CompositeItemProcessor<>();
        delegate.setDelegates(List.of(preparePagesNdkProcessor,
                enrichPagesNdkProcessor));
    }

    @Override
    public Page process(Page item) throws Exception {
        return delegate.process(item);
    }
}

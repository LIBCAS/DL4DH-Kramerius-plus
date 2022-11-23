package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.MetsEnricher;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesNdkProcessor implements ItemProcessor<Page, Page> {

    private final MetsEnricher metsEnricher;

    @Autowired
    public EnrichPagesNdkProcessor(MetsEnricher metsEnricher) {
        this.metsEnricher = metsEnricher;
    }

    @Override
    public Page process(@NonNull Page item) throws Exception {
        metsEnricher.enrich(item);

        return item;
    }
}

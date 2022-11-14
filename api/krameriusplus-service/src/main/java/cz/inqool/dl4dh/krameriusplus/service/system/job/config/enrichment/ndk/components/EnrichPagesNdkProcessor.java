package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.MetsEnricher;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.SkippingProcessor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesNdkProcessor extends SkippingProcessor<Page, Page> {

    private final MetsEnricher metsEnricher;

    @Autowired
    public EnrichPagesNdkProcessor(MetsEnricher metsEnricher) {
        this.metsEnricher = metsEnricher;
    }

    @Override
    protected Page doProcess(@NonNull Page item) throws Exception {
        metsEnricher.enrich(item);

        return item;
    }
}

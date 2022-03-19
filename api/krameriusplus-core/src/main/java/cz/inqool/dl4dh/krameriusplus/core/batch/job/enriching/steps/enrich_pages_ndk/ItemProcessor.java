package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages_ndk;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsEnricher;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Named(Steps.EnrichPagesNdk.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    private final MetsEnricher metsEnricher;

    @Autowired
    ItemProcessor(MetsEnricher metsEnricher) {
        this.metsEnricher = metsEnricher;
    }

    @Override
    public Page process(@NonNull Page page) {
        metsEnricher.enrich(page);

        return page;
    }
}

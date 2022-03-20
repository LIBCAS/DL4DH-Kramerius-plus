package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages.enrich_pages_alto;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingSteps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.AltoMetadataEnricher;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Named(EnrichingSteps.EnrichPagesAlto.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    @Override
    public Page process(@NonNull Page page) {
        new AltoMetadataEnricher(page).enrichPage();

        return page;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages.enrich_pages_nametag;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingSteps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.lindat.NameTagService;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Named(EnrichingSteps.EnrichPagesNameTag.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    private final NameTagService nameTagService;

    @Autowired
    public ItemProcessor(NameTagService nameTagService) {
        this.nameTagService = nameTagService;
    }

    @Override
    public Page process(@NonNull Page page) throws Exception {
        nameTagService.processTokens(page);

        return page;
    }
}

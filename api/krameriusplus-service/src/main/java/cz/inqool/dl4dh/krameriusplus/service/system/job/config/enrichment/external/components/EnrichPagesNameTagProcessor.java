package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.NameTagService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesNameTagProcessor implements ItemProcessor<Page, Page> {

    private final NameTagService nameTagService;

    @Autowired
    public EnrichPagesNameTagProcessor(NameTagService nameTagService) {
        this.nameTagService = nameTagService;
    }

    @Override
    public Page process(@NonNull Page item) {
        nameTagService.processTokens(item);

        return item;
    }
}

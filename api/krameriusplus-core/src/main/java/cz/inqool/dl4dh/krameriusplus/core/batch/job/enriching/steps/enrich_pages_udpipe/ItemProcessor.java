package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages_udpipe;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.lindat.UDPipeService;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Named(Steps.EnrichPagesUDPipe.PROCESSOR_NAME)
@StepScope
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    private final UDPipeService udPipeService;

    @Autowired
    ItemProcessor(UDPipeService udPipeService) {
        this.udPipeService = udPipeService;
    }

    @Override
    public Page process(@NonNull Page page) {
        udPipeService.tokenize(page);

        return page;
    }
}

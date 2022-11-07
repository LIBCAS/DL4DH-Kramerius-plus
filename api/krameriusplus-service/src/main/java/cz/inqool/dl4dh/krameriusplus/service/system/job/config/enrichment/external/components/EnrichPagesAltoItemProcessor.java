package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataEnricher;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.PageMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesAltoItemProcessor implements ItemProcessor<EnrichPageFromAltoDto, Page> {

    private final PageMapper pageMapper;

    @Autowired
    public EnrichPagesAltoItemProcessor(PageMapper pageMapper) {
        this.pageMapper = pageMapper;
    }


    @Override
    public Page process(@NonNull EnrichPageFromAltoDto item) {
        new AltoMetadataEnricher(item).enrichPage();

        return pageMapper.toPage(item);
    }
}

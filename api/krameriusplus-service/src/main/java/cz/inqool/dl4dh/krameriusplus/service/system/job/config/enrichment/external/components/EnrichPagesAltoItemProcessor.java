package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataEnricher;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesAltoItemProcessor implements ItemProcessor<EnrichPageFromAltoDto, Page> {

    @Override
    public Page process(@NonNull EnrichPageFromAltoDto item) {
        new AltoMetadataEnricher(item.getPage(), item.getAltoLayout()).enrichPage();

        return item.getPage();
    }
}

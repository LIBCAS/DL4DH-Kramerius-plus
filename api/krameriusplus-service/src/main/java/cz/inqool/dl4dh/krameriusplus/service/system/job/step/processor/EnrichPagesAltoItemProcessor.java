package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataEnricher;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.AltoWrappedPageDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesAltoItemProcessor implements ItemProcessor<AltoWrappedPageDto, Page> {

    private final AltoMetadataEnricher altoEnricher;

    @Autowired
    public EnrichPagesAltoItemProcessor(AltoMetadataEnricher altoEnricher) {
        this.altoEnricher = altoEnricher;
    }

    @Override
    public Page process(@NonNull AltoWrappedPageDto item) {
        altoEnricher.enrichPageTokens(item.getPage(), item.getAltoLayout());

        return item.getPage();
    }
}

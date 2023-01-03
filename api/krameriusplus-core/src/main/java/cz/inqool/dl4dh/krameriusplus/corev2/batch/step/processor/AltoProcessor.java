package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.AltoWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.alto.AltoMetadataEnricher;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class AltoProcessor implements ItemProcessor<AltoWrappedPage, Page> {

    private final AltoMetadataEnricher altoEnricher;

    @Autowired
    public AltoProcessor(AltoMetadataEnricher altoEnricher) {
        this.altoEnricher = altoEnricher;
    }

    @Override
    public Page process(@NonNull AltoWrappedPage item) {
        altoEnricher.enrichPageTokens(item.getPage(), item.getAltoLayout());

        return item.getPage();
    }
}

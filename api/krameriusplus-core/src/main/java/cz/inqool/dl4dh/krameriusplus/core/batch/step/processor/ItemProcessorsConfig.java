package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChain;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.processor.ItemProcessorsConfig.KrameriusProcessor.*;

@Configuration
public class ItemProcessorsConfig {

    @Bean(ENRICH_ALTO_COMPOSITE_PROCESSOR)
    @StepScope
    public CompositeItemProcessor<Page, Page> enrichAltoCompositeProcessor(
            AltoWrapperProcessor altoWrapperProcessor, UDPipeProcessor udPipeProcessor,
            NameTagProcessor nameTagProcessor, AltoProcessor altoProcessor) {
        return new CompositeItemProcessorBuilder<Page, Page>()
                .delegates(altoWrapperProcessor, udPipeProcessor, nameTagProcessor, altoProcessor)
                .build();
    }

    @Bean(CREATE_CHAINS_COMPOSITE_PROCESSOR)
    @StepScope
    public CompositeItemProcessor<String, List<EnrichmentChain>> createChainsCompositeProcessor(
            PublicationTreeFindingProcessor publicationTreeFindingProcessor,
            EnrichmentChainCreatingProcessor enrichmentChainCreatingProcessor) {
        return new CompositeItemProcessorBuilder<String, List<EnrichmentChain>>()
                .delegates(List.of(publicationTreeFindingProcessor, enrichmentChainCreatingProcessor))
                .build();
    }

    @Bean(ENRICH_NDK_COMPOSITE_PROCESSOR)
    @StepScope
    public CompositeItemProcessor<Page, Page> enrichNdkCompositeProcessor(
            NdkFilePathFinderProcessor filePathFinderProcessor,
            MetsProcessor metsProcessor) {
        return new CompositeItemProcessorBuilder<Page, Page>()
                .delegates(filePathFinderProcessor, metsProcessor)
                .build();
    }

    public static class KrameriusProcessor {

        public static final String ENRICH_ALTO_COMPOSITE_PROCESSOR = "ENRICH_ALTO_COMPOSITE_PROCESSOR";

        public static final String ENRICH_NDK_COMPOSITE_PROCESSOR = "ENRICH_NDK_COMPOSITE_PROCESSOR";

        public static final String CREATE_CHAINS_COMPOSITE_PROCESSOR = "CREATE_CHAINS_COMPOSITE_PROCESSOR";
    }
}

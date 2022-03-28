package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsEnricher;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingStep.ENRICH_PAGES_NDK;

@Configuration
public class EnrichPagesNdk {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesNdkStep(ItemReader<Page> reader,
                                   ItemProcessor<Page, Page> enrichPagesNdkProcessor,
                                   MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(ENRICH_PAGES_NDK)
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(enrichPagesNdkProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    ItemProcessor<Page, Page> enrichPagesNdkProcessor(MetsEnricher metsEnricher) {
        return page -> {
            metsEnricher.enrich(page);

            return page;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

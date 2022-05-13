package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.lindat.NameTagService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.ENRICH_PAGES_NAME_TAG;

@Configuration
public class EnrichPagesNameTag {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesNameTagStep(ItemReader<Page> reader,
                                       ItemProcessor<Page, Page> enrichPagesNameTagProcessor,
                                       MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(ENRICH_PAGES_NAME_TAG)
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(enrichPagesNameTagProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Page, Page> enrichPagesNameTagProcessor(NameTagService nameTagService) {
        return page -> {
            nameTagService.processTokens(page);

            return page;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

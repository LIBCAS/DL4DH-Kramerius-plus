package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.enrich_external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.JobStep;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CleanUpPages {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step cleanUpPagesStep(ItemReader<Page> reader,
                                 ItemProcessor<Page, Page> cleanUpProcessor,
                                 MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(JobStep.CLEAN_UP_PAGES)
                .allowStartIfComplete(true)
                .<Page, Page>chunk(5)
                .reader(reader)
                .processor(cleanUpProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    ItemProcessor<Page, Page> cleanUpProcessor() {
        return item -> {
            item.setAltoLayout(null);
            item.setContent(null);

            return item;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

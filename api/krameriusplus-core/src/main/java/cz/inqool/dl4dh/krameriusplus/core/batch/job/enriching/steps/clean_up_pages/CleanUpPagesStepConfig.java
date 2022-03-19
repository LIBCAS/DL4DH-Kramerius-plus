package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.clean_up_pages;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PageMongoItemReader;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PageMongoItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps.CleanUpPages.STEP_NAME;

@Configuration
public class CleanUpPagesStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private PageMongoItemReader reader;

    private ItemProcessor processor;

    private PageMongoItemWriter writer;

    @Bean(name = STEP_NAME)
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Page, Page>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setReader(PageMongoItemReader reader) {
        this.reader = reader;
    }

    @Resource(name = Steps.CleanUpPages.PROCESSOR_NAME)
    public void setProcessor(ItemProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(PageMongoItemWriter writer) {
        this.writer = writer;
    }
}

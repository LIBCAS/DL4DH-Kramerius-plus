package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_pages.enrich_pages_nametag;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PageMongoItemReader;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PageMongoItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingSteps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingSteps.EnrichPagesNameTag.STEP_NAME;

@Configuration
public class EnrichPagesNameTagStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private PageMongoItemReader reader;

    private ItemProcessor processor;

    private PageMongoItemWriter writer;

    @Bean(name = STEP_NAME)
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Page, Page> chunk(5)
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

    @Resource(name = EnrichingSteps.EnrichPagesNameTag.PROCESSOR_NAME)
    public void setProcessor(ItemProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(PageMongoItemWriter writer) {
        this.writer = writer;
    }
}

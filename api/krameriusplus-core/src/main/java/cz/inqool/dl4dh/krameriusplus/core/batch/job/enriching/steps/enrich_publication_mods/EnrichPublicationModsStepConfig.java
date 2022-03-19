package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.enrich_publication_mods;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PublicationItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PublicationMongoItemReader;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps.EnrichPublicationMods.STEP_NAME;

@Configuration
public class EnrichPublicationModsStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private PublicationMongoItemReader reader;

    private ItemProcessor processor;

    private PublicationItemWriter writer;

    @Bean(name = STEP_NAME)
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Publication, Publication>chunk(1)
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
    public void setReader(PublicationMongoItemReader reader) {
        this.reader = reader;
    }

    @Resource(name = Steps.EnrichPublicationMods.PROCESSOR_NAME)
    public void setProcessor(ItemProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(PublicationItemWriter writer) {
        this.writer = writer;
    }
}

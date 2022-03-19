package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.download_publication;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.PublicationItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps.DownloadPublicationStep.STEP_NAME;

@Configuration
public class DownloadPublicationStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private ItemReader reader;

    private ItemProcessor processor;

    private PublicationItemWriter writer;

    @Bean(name = STEP_NAME)
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<String, Publication> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Resource(name = Steps.DownloadPublicationStep.READER_NAME)
    public void setReader(ItemReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(ItemProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(PublicationItemWriter writer) {
        this.writer = writer;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.generate_file;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingSteps;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.common.PublicationWithPagesItemReader;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingSteps.GenerateFileStep.STEP_NAME;


@Configuration
public class GenerateFileStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private EntityManagerFactory entityManagerFactory;

    @Bean(name = STEP_NAME)
    public Step step(PublicationWithPagesItemReader reader,
                     @Qualifier(JsonExportingSteps.GenerateFileStep.PROCESSOR_NAME) ItemProcessor processor,
                     @Qualifier(JsonExportingSteps.GenerateFileStep.WRITER_NAME) ItemWriter<FileRef> writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Publication, FileRef> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(promotionListener())
                .build();
    }

    @Bean(JsonExportingSteps.GenerateFileStep.WRITER_NAME)
    @StepScope
    public JpaItemWriter<FileRef> writer() {
        return new JpaItemWriterBuilder<FileRef>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {"fileRefId", "publicationTitle"});

        return listener;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.save_export;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingSteps;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.common.DomainItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingSteps.SaveExportStep.STEP_NAME;

@Configuration
public class SaveExportStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private EntityManagerFactory entityManagerFactory;

    @Bean(name = STEP_NAME)
    public Step step(SingleJpaItemReader reader,
                     @Qualifier(JsonExportingSteps.SaveExportStep.PROCESSOR_NAME) ItemProcessor<FileRef, Export> processor,
                     @Qualifier(JsonExportingSteps.SaveExportStep.WRITER_NAME) ItemWriter<Export> writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<FileRef, Export> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public SingleJpaItemReader reader() {
        SingleJpaItemReader reader = new SingleJpaItemReader();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select f from FileRef f where f.id = :id");

        return reader;
    }

    @Bean(JsonExportingSteps.SaveExportStep.PROCESSOR_NAME)
    @StepScope
    public FunctionItemProcessor<FileRef, Export> processor(@Value("#{jobParameters['publicationId']}") String publicationId,
                                                            @Value("#{jobParameters['publicationTitle']}") String publicationTitle) {
        return new FunctionItemProcessor<>(fileRef -> {
            Export export = new Export();
            export.setPublicationId(publicationId);
            export.setPublicationTitle(publicationTitle);
            export.setFileRef(fileRef);

            return export;
        });
    }

    @Bean(JsonExportingSteps.SaveExportStep.WRITER_NAME)
    @StepScope
    public DomainItemWriter<Export> writer(ExportStore exportStore) {
        return new DomainItemWriter<>(exportStore);
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

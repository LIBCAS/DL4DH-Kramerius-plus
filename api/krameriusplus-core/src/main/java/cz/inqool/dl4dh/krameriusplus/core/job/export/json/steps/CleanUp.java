package cz.inqool.dl4dh.krameriusplus.core.job.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Files;

@Configuration
public class CleanUp {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step cleanUpStep(Tasklet cleanUpTasklet) {
        return stepBuilderFactory.get(JsonExportingSteps.CLEAN_UP)
                .tasklet(cleanUpTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet cleanUpTasklet(@Value("#{jobParameters['publicationId']}") String publicationId) {
        return (contribution, chunkContext) -> {
            Files.delete(new FileSystemResource("tmp_" + ExportFormat.JSON.getFileName(publicationId)).getFile().toPath());

            return RepeatStatus.FINISHED;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

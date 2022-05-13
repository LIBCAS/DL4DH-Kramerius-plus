package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.*;

@Configuration
public class SaveFile {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step saveFileStep(Tasklet saveFileTasklet) {
        return stepBuilderFactory.get(JsonExportingSteps.SAVE_FILE)
                .tasklet(saveFileTasklet)
                .listener(promotionListener())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet saveFileTasklet(FileService fileService,
                            @Value("#{jobParameters['publicationId']}") String publicationId) {
        return (contribution, chunkContext) -> {
            File file = new FileSystemResource("tmp_" + ExportFormat.JSON.getFileName(publicationId)).getFile();

            try (InputStream is = new FileInputStream(file)) {
                FileRef fileRef = fileService.create(
                        is,
                        file.length(),
                        ExportFormat.JSON.getFileName(publicationId),
                        ContentType.APPLICATION_JSON.getMimeType());

                chunkContext.getStepContext().getStepExecution().getExecutionContext().put("fileRefId", fileRef.getId());

                return RepeatStatus.FINISHED;
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Could not write publication to string.");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }


    @Bean
    @StepScope
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[]{"fileRefId"});

        return listener;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRefStore;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaveExport {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step saveExportStep(Tasklet saveExportTasklet) {
        return stepBuilderFactory.get(JsonExportingSteps.SAVE_EXPORT)
                .tasklet(saveExportTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet saveExportTasklet(ExportStore exportStore,
                              FileRefStore fileRefStore,
                              @Value("#{jobParameters['publicationId']}") String publicationId,
                              @Value("#{jobParameters['publicationTitle']}") String publicationTitle,
                              @Value("#{jobExecutionContext['fileRefId']}") String fileRefId) {
        return (contribution, chunkContext) -> {
            FileRef fileRef = fileRefStore.find(fileRefId);

            Export export = new Export();
            export.setPublicationId(publicationId);
            export.setPublicationTitle(publicationTitle);
            export.setFileRef(fileRef);

            exportStore.create(export);

            return RepeatStatus.FINISHED;
        };
    }


    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.JobStep.EXPORT_STEP;

@Configuration
@Slf4j
public class ExportStep {

    private StepBuilderFactory stepBuilderFactory;

    private ExporterService exporterService;

    private ObjectMapper objectMapper;

    @Bean
    public Step exportingStep() {
        return stepBuilderFactory.get(EXPORT_STEP)
                .tasklet(exportingTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet exportingTasklet() {
        return (contribution, chunkContext) -> {
            JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();
            String publicationId = jobParameters.getString("publicationId");
            Params params = objectMapper.readValue(jobParameters.getString("params"), Params.class);
            ExportFormat exportFormat = ExportFormat.fromString(jobParameters.getString("exportFormat"));

            exporterService.export(publicationId, params, exportFormat);

            return RepeatStatus.FINISHED;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setExporterService(ExporterService exporterService) {
        this.exporterService = exporterService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

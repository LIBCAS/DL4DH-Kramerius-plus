package cz.inqool.dl4dh.krameriusplus.core.job.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExportingJobConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private ExporterService exporterService;

    private ObjectMapper objectMapper;

    @Bean
    public Job exportingJob() {
        return jobBuilderFactory.get(KrameriusJob.EXPORTING_JOB.name())
                .validator(validator())
                .incrementer(new RunIdIncrementer())
                .start(exportingStep())
                .build();
    }

    @Bean
    public Step exportingStep() {
        return stepBuilderFactory.get("EXPORTING_STEP")
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

    private JobParametersValidator validator() {
        return parameters -> {
            if (parameters == null) {
                throw new NullPointerException("jobParameters");
            }

            Map<String, Object> invalidParameters = new HashMap<>();

            if (parameters.getString("publicationId") == null) {
                invalidParameters.put("publicationId", null);
            }

            if (parameters.getString("params") == null) {
                invalidParameters.put("params", null);
            }

            if (parameters.getString("exportFormat") == null) {
                invalidParameters.put("exportFormat", null);
            }

            if (Arrays.stream(ExportFormat.values())
                    .noneMatch(value -> value.name().equals(parameters.getString("exportFormat")))) {
                invalidParameters.put("exportFormat", parameters.getString("exportFormat"));
            }

            if (!invalidParameters.isEmpty()) {
                throw new JobParametersInvalidException("Invalid parameters: " + invalidParameters);
            }
        };
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
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

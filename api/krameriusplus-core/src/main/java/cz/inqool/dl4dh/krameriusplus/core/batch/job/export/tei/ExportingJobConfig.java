package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.tei;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExporterService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExportingJobConfig {

    public static final String EXPORTING_JOB = "exportingJob";

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private ExporterService exporterService;

    private ObjectMapper objectMapper;

    @Bean(EXPORTING_JOB)
    public Job teiExportingJob() {
        return jobBuilderFactory.get(EXPORTING_JOB)
                .validator(parameters -> {
                    if (parameters == null ||
                            parameters.getString("publicationId") == null ||
                            parameters.getString("params") == null ||
                            parameters.getString("exportFormat") == null) {
                        throw new JobParametersInvalidException("Parameter 'publicationId', 'params' or 'exportFormat' missing.");
                    }
                })
                .incrementer(new RunIdIncrementer())
                .start(exportingStep())
                .build();
    }

    @Bean
    public Step exportingStep() {
        return stepBuilderFactory.get("TEI_EXPORTING_STEP")
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

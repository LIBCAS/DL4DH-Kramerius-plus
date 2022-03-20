package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class JsonExportingJobConfig {

    public static final String JSON_EXPORTING_JOB = "jsonExportingJob";

    private JobBuilderFactory jobBuilderFactory;

    private final Map<String, Step> steps = new HashMap<>();

    @Bean(JSON_EXPORTING_JOB)
    public Job jsonExportingJob() {
        return jobBuilderFactory.get(JSON_EXPORTING_JOB)
                .incrementer(new RunIdIncrementer())
                .start(steps.get(JsonExportingSteps.GenerateFileStep.STEP_NAME))
                .next(steps.get(JsonExportingSteps.SaveExportStep.STEP_NAME))
                .build();
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setSteps(List<Step> steps) {
        steps.forEach(step -> this.steps.put(step.getName(), step));
    }
}

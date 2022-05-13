package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json.steps.JsonExportingSteps;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
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
                .validator(parameters -> {
                    if (parameters == null || parameters.getString("publicationId") == null) {
                        throw new JobParametersInvalidException("Parameter 'publicationId' missing.");
                    }
                })
                .incrementer(new RunIdIncrementer())
                .start(steps.get(JsonExportingSteps.WRITE_PUBLICATION_START))
                .next(steps.get(JsonExportingSteps.WRITE_PAGES))
                .next(steps.get(JsonExportingSteps.WRITE_PUBLICATION_END))
                .next(steps.get(JsonExportingSteps.SAVE_FILE))
                .next(steps.get(JsonExportingSteps.SAVE_EXPORT))
                .next(steps.get(JsonExportingSteps.CLEAN_UP))
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

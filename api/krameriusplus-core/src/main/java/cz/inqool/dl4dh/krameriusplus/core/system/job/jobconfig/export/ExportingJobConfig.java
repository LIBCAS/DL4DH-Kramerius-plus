package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export;

import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.CommonJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.EXPORT_STEP;

@Configuration
public class ExportingJobConfig extends CommonJobConfig {


    @Bean
    public Job exportingJob(JobListener listener) {
        return jobBuilderFactory.get(KrameriusJob.EXPORT.name())
                .validator(validator())
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(steps.get(EXPORT_STEP))
                .build();
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
}

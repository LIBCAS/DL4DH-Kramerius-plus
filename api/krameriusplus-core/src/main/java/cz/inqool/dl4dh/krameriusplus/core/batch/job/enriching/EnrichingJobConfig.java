package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
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
public class EnrichingJobConfig {

    public static final String ENRICHING_JOB = "enrichingJob";

    private JobBuilderFactory jobBuilderFactory;

    private final Map<String, Step> steps = new HashMap<>();

    @Bean(ENRICHING_JOB)
    public Job enrichingJob() {
        return jobBuilderFactory
                .get(ENRICHING_JOB)
                .incrementer(new RunIdIncrementer())
                .start(steps.get(Steps.DownloadPublicationStep.STEP_NAME))
                .next(steps.get(Steps.EnrichPublicationMods.STEP_NAME))
                .next(steps.get(Steps.EnrichPublicationTei.STEP_NAME))

                .next(steps.get(Steps.PrepareNdkPublicationPath.STEP_NAME))
                .on("COMPLETED")
                .to(steps.get(Steps.PrepareNdkPagesPath.STEP_NAME))
                .from(steps.get(Steps.PrepareNdkPublicationPath.STEP_NAME))
                .on("*")
                .to(steps.get(Steps.DownloadPagesAltoStep.STEP_NAME))

                .from(steps.get(Steps.PrepareNdkPagesPath.STEP_NAME))
                .on("COMPLETED")
                .to(steps.get(Steps.EnrichPagesNdk.STEP_NAME))
                .from(steps.get(Steps.PrepareNdkPagesPath.STEP_NAME))
                .on("*")
                .to(steps.get(Steps.DownloadPagesAltoStep.STEP_NAME))

                .from(steps.get(Steps.EnrichPagesNdk.STEP_NAME))

                .next(steps.get(Steps.DownloadPagesAltoStep.STEP_NAME))
                .next(steps.get(Steps.EnrichPagesUDPipe.STEP_NAME))

                .next(steps.get(Steps.EnrichPagesNameTag.STEP_NAME))
                .next(steps.get(Steps.EnrichPagesAlto.STEP_NAME))
                .next(steps.get(Steps.EnrichPagesTei.STEP_NAME))
                .next(steps.get(Steps.CleanUpPages.STEP_NAME))
                .end()
                .build();
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setDownloadingPublicationStep(List<Step> steps) {
        steps.forEach(step -> this.steps.put(step.getName(), step));
    }
}

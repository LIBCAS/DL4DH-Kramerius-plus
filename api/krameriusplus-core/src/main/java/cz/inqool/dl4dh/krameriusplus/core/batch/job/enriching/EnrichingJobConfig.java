package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.KrameriusJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingStep.*;

@Configuration
public class EnrichingJobConfig {

    private JobBuilderFactory jobBuilderFactory;

    private final Map<String, Step> steps = new HashMap<>();

    @Bean
    public Job enrichingJob() {
        return jobBuilderFactory
                .get(KrameriusJob.ENRICH_EXTERNAL.name())
                .validator(parameters -> {
                    if (parameters == null || parameters.getString("publicationId") == null) {
                        throw new JobParametersInvalidException("Parameter 'publicationId' missing.");
                    }
                })
                .incrementer(new RunIdIncrementer())
                .start(downloadFlow())
                .next(ndkFlow()).on("*").to(steps.get(ENRICH_PUBLICATION_TEI))
                .next(enrichPagesFlow()).on("FAILED").fail().next(steps.get(CLEAN_UP_PAGES))
                .from(enrichPagesFlow()).on("*").to(steps.get(CLEAN_UP_PAGES))
                .end()
                .build();
    }

    @Bean
    Flow downloadFlow() {
        return new FlowBuilder<SimpleFlow>("downloadFlow")
                .start(steps.get(DOWNLOAD_PUBLICATION))
                .next(steps.get(DOWNLOAD_PUBLICATION_CHILDREN))
                .end();
    }

    @Bean
    Flow ndkFlow() {
        return new FlowBuilder<SimpleFlow>("ndkFlow")
                .start(steps.get(PREPARE_PUBLICATION_NDK))
                .next(steps.get(ENRICH_PUBLICATION_NDK))
                .next(steps.get(PREPARE_PAGES_NDK))
                .next(steps.get(ENRICH_PAGES_NDK))
                .end();
    }

    @Bean
    Flow enrichPagesFlow() {
        return new FlowBuilder<SimpleFlow>("enrichPagesFlow")
                .start(steps.get(DOWNLOAD_PAGES_ALTO))
                .next(steps.get(ENRICH_PAGES_UD_PIPE))
                .next(steps.get(ENRICH_PAGES_NAME_TAG))
                .next(steps.get(ENRICH_PAGES_ALTO))
                .next(steps.get(ENRICH_PAGES_TEI))
                .end();
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

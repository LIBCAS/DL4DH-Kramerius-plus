package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.prepare_ndk.prepare_ndk_pages_path;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingSteps.PrepareNdkPagesPath.STEP_NAME;

@Configuration
public class PrepareNdkPagesPathStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private PagesNdkFileFindingTasklet tasklet;

    @Bean(name = STEP_NAME)
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(tasklet)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setTasklet(PagesNdkFileFindingTasklet tasklet) {
        this.tasklet = tasklet;
    }
}


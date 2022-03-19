package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.prepare_ndk_publication_path;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps.PrepareNdkPublicationPath.STEP_NAME;

@Configuration
public class PrepareNdkPublicationPathStepConfig {

    private StepBuilderFactory stepBuilderFactory;

    private PublicationNdkFileFindingTasklet tasklet;

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
    public void setTasklet(PublicationNdkFileFindingTasklet tasklet) {
        this.tasklet = tasklet;
    }
}

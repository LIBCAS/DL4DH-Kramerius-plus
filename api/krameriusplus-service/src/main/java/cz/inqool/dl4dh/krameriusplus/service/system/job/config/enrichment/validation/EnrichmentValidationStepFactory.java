package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICHMENT_VALIDATION;

@Component
public class EnrichmentValidationStepFactory extends AbstractStepFactory {

    private final EnrichmentValidationTasklet tasklet;

    @Autowired
    public EnrichmentValidationStepFactory(EnrichmentValidationTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(ENRICHMENT_VALIDATION)
                .tasklet(tasklet)
                .build();
    }
}

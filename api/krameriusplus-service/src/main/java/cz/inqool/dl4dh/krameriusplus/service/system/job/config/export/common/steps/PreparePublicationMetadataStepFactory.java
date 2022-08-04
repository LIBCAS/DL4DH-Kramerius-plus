package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PreparePublicationMetadataTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_METADATA;

@Component
public class PreparePublicationMetadataStepFactory extends AbstractStepFactory {

    private final PreparePublicationMetadataTasklet tasklet;

    @Autowired
    public PreparePublicationMetadataStepFactory(PreparePublicationMetadataTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return PREPARE_PUBLICATION_METADATA;
    }
}

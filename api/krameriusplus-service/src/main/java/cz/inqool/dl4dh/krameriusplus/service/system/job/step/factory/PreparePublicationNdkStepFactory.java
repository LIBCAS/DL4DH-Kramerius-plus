package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PreparePublicationNdkProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.PREPARE_PUBLICATION_NDK;

@Component
public class PreparePublicationNdkStepFactory extends PublicationMongoFlowStepFactory {

    private final PreparePublicationNdkProcessor processor;

    @Autowired
    public PreparePublicationNdkStepFactory(PreparePublicationNdkProcessor processor) {
        this.processor = processor;
    }

    @Bean(PREPARE_PUBLICATION_NDK)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected String getStepName() {
        return PREPARE_PUBLICATION_NDK;
    }

    @Override
    protected ItemProcessor<Publication, Publication> getItemProcessor() {
        return processor;
    }
}

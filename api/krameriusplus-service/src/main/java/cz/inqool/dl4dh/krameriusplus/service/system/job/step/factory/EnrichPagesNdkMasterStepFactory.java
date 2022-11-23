package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.EnrichmentSuccessDecidingAggregator;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_NDK_MASTER;

@Component
public class EnrichPagesNdkMasterStepFactory extends PartitionedStepFactory {

    private Step enrichPagesNdkStep;

    private EnrichmentSuccessDecidingAggregator aggregator;

    @Bean(ENRICH_PAGES_NDK_MASTER)
    @Override
    public Step build() {
        return getBuilder().aggregator(aggregator).build();
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_NDK_MASTER;
    }

    @Override
    protected Step getPartitionedStep() {
        return enrichPagesNdkStep;
    }

    @Autowired
    public void setEnrichPagesNdkStep(@Qualifier(ENRICH_PAGES_NDK) Step enrichPagesNdkStep) {
        this.enrichPagesNdkStep = enrichPagesNdkStep;
    }

    @Autowired
    public void setAggregator(EnrichmentSuccessDecidingAggregator aggregator) {
        this.aggregator = aggregator;
    }
}

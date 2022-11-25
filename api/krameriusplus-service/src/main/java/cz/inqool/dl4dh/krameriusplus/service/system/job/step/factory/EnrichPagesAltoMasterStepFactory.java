package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.EnrichmentSuccessDecidingAggregator;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_ALTO_MASTER;

@Component
public class EnrichPagesAltoMasterStepFactory extends PartitionedStepFactory {

    private EnrichmentSuccessDecidingAggregator aggregator;

    private Step enrichPagesFromAltoStep;

    @Bean(ENRICH_PAGES_ALTO_MASTER)
    @Override
    public Step build() {
        return getBuilder()
                .aggregator(aggregator)
                .build();
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_ALTO_MASTER;
    }

    @Override
    protected Step getPartitionedStep() {
        return enrichPagesFromAltoStep;
    }

    @Autowired
    public void setEnrichPagesFromAltoStep(@Qualifier(ENRICH_PAGES_ALTO) Step enrichPagesFromAltoStep) {
        this.enrichPagesFromAltoStep = enrichPagesFromAltoStep;
    }

    @Autowired
    public void setAggregator(EnrichmentSuccessDecidingAggregator aggregator) {
        this.aggregator = aggregator;
    }
}

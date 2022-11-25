package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.EnrichmentSuccessDecidingAggregator;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_TEI_MASTER;

@Component
public class EnrichPagesTeiMasterStepFactory extends PartitionedStepFactory {

    private Step enrichPagesTeiStep;

    private EnrichmentSuccessDecidingAggregator aggregator;

    @Bean(ENRICH_PAGES_TEI_MASTER)
    @Override
    public Step build() {
        return getBuilder()
                .aggregator(aggregator)
                .build();
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_TEI_MASTER;
    }

    @Override
    protected Step getPartitionedStep() {
        return enrichPagesTeiStep;
    }

    @Autowired
    public void setEnrichPagesTeiStep(@Qualifier(ENRICH_PAGES_TEI) Step enrichPagesTeiStep) {
        this.enrichPagesTeiStep = enrichPagesTeiStep;
    }

    @Autowired
    public void setAggregator(EnrichmentSuccessDecidingAggregator aggregator) {
        this.aggregator = aggregator;
    }
}

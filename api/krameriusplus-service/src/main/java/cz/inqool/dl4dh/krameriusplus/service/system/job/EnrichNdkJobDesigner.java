package cz.inqool.dl4dh.krameriusplus.service.system.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_NDK_MASTER;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.PREPARE_PUBLICATION_NDK;

@Configuration
public class EnrichNdkJobDesigner extends EnrichmentJobDesigner {

    private Step preparePublicationNdkStep;

    private Step enrichPagesNdkMasterStep;

    @Bean
    public Job enrichNdkJob() {
        return super.getJobBuilder()
                .next(preparePublicationNdkStep)
                .next(enrichPagesNdkMasterStep)
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK.name();
    }

    @Autowired
    public void setPreparePublicationNdkStep(@Qualifier(PREPARE_PUBLICATION_NDK) Step preparePublicationNdkStep) {
        this.preparePublicationNdkStep = preparePublicationNdkStep;
    }

    @Autowired
    public void setEnrichPagesNdkMasterStep(@Qualifier(ENRICH_PAGES_NDK_MASTER) Step enrichPagesNdkMasterStep) {
        this.enrichPagesNdkMasterStep = enrichPagesNdkMasterStep;
    }
}

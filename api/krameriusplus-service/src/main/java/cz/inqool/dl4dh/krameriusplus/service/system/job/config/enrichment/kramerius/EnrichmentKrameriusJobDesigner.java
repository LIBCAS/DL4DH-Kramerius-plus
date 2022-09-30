package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_DIGITAL_OBJECTS;

@Configuration
public class EnrichmentKrameriusJobDesigner extends EnrichmentJobDesigner {

    @Bean
    public Job enrichingJob() {
        return super.getJobBuilder()
                .next(stepContainer.getStep(DOWNLOAD_DIGITAL_OBJECTS))
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.ENRICHMENT_KRAMERIUS.name();
    }
}

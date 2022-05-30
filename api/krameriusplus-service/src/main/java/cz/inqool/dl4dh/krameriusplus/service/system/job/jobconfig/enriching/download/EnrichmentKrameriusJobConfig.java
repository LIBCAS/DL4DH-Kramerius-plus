package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.download;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.EnrichmentJobValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.JobStep.*;

@Configuration
public class EnrichmentKrameriusJobConfig extends EnrichmentBaseJobConfig {

    @Autowired
    public EnrichmentKrameriusJobConfig(EnrichmentJobValidator validator) {
        super(validator);
    }

    @Bean
    public Job enrichingJob() {
        return super.getJobBuilder()
                .start(steps.get(ENRICHMENT_VALIDATION))
                .next(steps.get(DOWNLOAD_PUBLICATION))
                .next(steps.get(DOWNLOAD_PUBLICATION_CHILDREN))
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.ENRICHMENT_KRAMERIUS.name();
    }
}

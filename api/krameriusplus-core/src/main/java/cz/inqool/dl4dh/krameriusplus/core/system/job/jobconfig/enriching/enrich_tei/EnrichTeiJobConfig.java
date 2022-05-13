package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_tei;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.CommonJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob.ENRICH_TEI;
import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.ENRICH_PAGES_TEI;
import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.ENRICH_PUBLICATION_TEI;

@Configuration
public class EnrichTeiJobConfig extends CommonJobConfig {

    @Bean
    public Job enrichTeiJob() {
        return jobBuilderFactory.get(ENRICH_TEI.name())
                .listener(jobListener)
                .start(steps.get(ENRICH_PUBLICATION_TEI))
                .next(steps.get(ENRICH_PAGES_TEI))
                .build();
    }
}

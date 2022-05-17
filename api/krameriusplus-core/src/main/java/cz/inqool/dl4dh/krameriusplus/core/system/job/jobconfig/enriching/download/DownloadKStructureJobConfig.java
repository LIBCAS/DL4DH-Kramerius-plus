package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.download;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.CommonJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.PublicationJobValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.DOWNLOAD_PUBLICATION;
import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.DOWNLOAD_PUBLICATION_CHILDREN;

@Configuration
public class DownloadKStructureJobConfig extends CommonJobConfig {

    @Bean
    public Job enrichingJob(PublicationJobValidator validator) {
        return jobBuilderFactory
                .get(KrameriusJob.ENRICHMENT_KRAMERIUS.name())
                .validator(validator)
                .listener(jobListener)
                .incrementer(new RunIdIncrementer())
                .start(steps.get(DOWNLOAD_PUBLICATION))
                .next(steps.get(DOWNLOAD_PUBLICATION_CHILDREN))
                .build();
    }
}

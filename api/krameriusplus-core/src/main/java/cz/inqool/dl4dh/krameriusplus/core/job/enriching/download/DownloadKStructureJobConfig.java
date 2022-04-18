package cz.inqool.dl4dh.krameriusplus.core.job.enriching.download;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.CommonJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.PublicationJobValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.DOWNLOAD_PUBLICATION;
import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.DOWNLOAD_PUBLICATION_CHILDREN;

@Configuration
public class DownloadKStructureJobConfig extends CommonJobConfig {

    @Bean
    public Job enrichingJob(PublicationJobValidator validator) {
        return jobBuilderFactory
                .get(KrameriusJob.DOWNLOAD_K_STRUCTURE.name())
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(steps.get(DOWNLOAD_PUBLICATION))
                .next(steps.get(DOWNLOAD_PUBLICATION_CHILDREN))
                .build();
    }
}

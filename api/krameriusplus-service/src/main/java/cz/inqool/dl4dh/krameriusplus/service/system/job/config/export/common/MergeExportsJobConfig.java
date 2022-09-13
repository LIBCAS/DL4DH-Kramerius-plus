package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.EXPORT_MERGE;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_BULK_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.UNZIP_EXPORTS;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ZIP_EXPORT;

@Configuration
public class MergeExportsJobConfig extends ExportJobConfig {

    @Bean
    public Job mergeExportsJob() {
        return getJobBuilder()
                .next(stepContainer.getStep(UNZIP_EXPORTS)).on("MERGE_DONE").end()
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_BULK_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .end()
                .build();
    }


    @Override
    public String getJobName() {
        return EXPORT_MERGE.name();
    }
}

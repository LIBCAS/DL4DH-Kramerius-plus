package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.merge;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.EXPORT_MERGE;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class MergeExportsJobConfig extends JobConfigBase {

    @Bean
    public Job mergeExportsJob() {
        return getJobBuilder()
                .next(unzipExports()).on("MERGE_DONE").to(createBulkFileRef())
                .from(unzipExports()).on("*").to(zipExport())
                .from(zipExport())
                .next(createBulkFileRef())
                .next(stepContainer.getStep(CREATE_BULK_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .end()
                .build();
    }

    private Step unzipExports() {
        return stepContainer.getStep(UNZIP_EXPORTS);
    }

    private Step createBulkFileRef() {
        return stepContainer.getStep(CREATE_BULK_FILE_REF);
    }

    private Step zipExport() {
        return stepContainer.getStep(ZIP_EXPORT);
    }

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
    }

    @Override
    public String getJobName() {
        return EXPORT_MERGE.name();
    }
}

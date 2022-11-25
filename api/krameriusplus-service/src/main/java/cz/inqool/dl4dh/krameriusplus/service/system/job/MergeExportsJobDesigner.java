package cz.inqool.dl4dh.krameriusplus.service.system.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.EXPORT_MERGE;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.*;

@Configuration
public class MergeExportsJobDesigner extends AbstractJobDesigner {

    private Step unzipExportsStep;

    private Step createBulkFileRefStep;

    private Step createBulkExportStep;

    private Step zipExportStep;

    private Step cleanUpExportStep;

    @Bean
    public Job mergeExportsJob() {
        return getJobBuilder()
                .next(unzipExportsStep).on("MERGE_DONE").to(createBulkExportStep)
                .from(unzipExportsStep).on("*").to(zipExportStep)
                .from(zipExportStep)
                .next(createBulkFileRefStep)
                .next(createBulkExportStep).on("NO_CLEANUP").end()
                .from(createBulkExportStep).on("*").to(cleanUpExportStep).end()
                .build();
    }

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
    }

    @Override
    public String getJobName() {
        return EXPORT_MERGE.name();
    }

    @Autowired
    public void setUnzipExportsStep(@Qualifier(UNZIP_EXPORTS) Step unzipExportsStep) {
        this.unzipExportsStep = unzipExportsStep;
    }

    @Autowired
    public void setCreateBulkFileRefStep(@Qualifier(CREATE_BULK_FILE_REF) Step createBulkFileRefStep) {
        this.createBulkFileRefStep = createBulkFileRefStep;
    }

    @Autowired
    public void setCreateBulkExportStep(@Qualifier(CREATE_BULK_EXPORT) Step createBulkExportStep) {
        this.createBulkExportStep = createBulkExportStep;
    }

    @Autowired
    public void setZipExportStep(@Qualifier(ZIP_EXPORT) Step zipExportStep) {
        this.zipExportStep = zipExportStep;
    }

    @Autowired
    public void setCleanUpExportStep(@Qualifier(CLEAN_UP_EXPORT) Step cleanUpExportStep) {
        this.cleanUpExportStep = cleanUpExportStep;
    }
}

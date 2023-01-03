package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CreateMergeJobTasklet implements Tasklet {

    private KrameriusJobInstanceService jobInstanceService;

    @Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}")
    private String exportRequestId;

    private ExportRequestStore exportRequestStore;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(JobParameterKey.EXPORT_REQUEST_ID, exportRequestId);

        KrameriusJobInstance mergeJob = jobInstanceService.createJobInstance(KrameriusJobType.MERGE_JOB, jobParameters);

        ExportRequest exportRequest = exportRequestStore.findById(exportRequestId).orElseThrow();
        exportRequest.getBulkExport().setMergeJob(mergeJob);
        exportRequestStore.save(exportRequest);

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setExportRequestStore(ExportRequestStore exportRequestStore) {
        this.exportRequestStore = exportRequestStore;
    }

    @Autowired
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }
}

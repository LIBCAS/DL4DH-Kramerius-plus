package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class CreateMergeJobTasklet implements Tasklet {

    private KrameriusJobInstanceService jobInstanceService;

    @Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}")
    private String exportRequestId;

    private ExportRequestStore exportRequestStore;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParametersMapWrapper jobParameters = new JobParametersMapWrapper();
        jobParameters.putString(EXPORT_REQUEST_ID, exportRequestId);

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

package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
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

import java.util.List;

@Component
@StepScope
public class ExportItemsEnqueuingTasklet implements Tasklet {

    private final ExportRequest exportRequest;

    private final JobEnqueueService jobEnqueueService;

    @Autowired
    public ExportItemsEnqueuingTasklet(@Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                           ExportRequestStore exportRequestStore,
                                           JobEnqueueService jobEnqueueService) {
        this.exportRequest = exportRequestStore.findById(exportRequestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, exportRequestId));
        this.jobEnqueueService = jobEnqueueService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        exportRequest.getItems().forEach(
                item -> {
                    Export firstLeafNode = findFirstLeafNode(item.getRootExport());

                    jobEnqueueService.enqueue(firstLeafNode.getExportJob());
                }
        );

        return RepeatStatus.FINISHED;
    }

    private Export findFirstLeafNode(Export rootExport) {
        boolean isMaxDepth = false;

        Export current = rootExport;
        while (!isMaxDepth) {
            List<Export> children = current.getChildrenList();

            if (children.isEmpty()) {
                isMaxDepth = true;
            } else {
                current = children.iterator().next(); // get first child
            }
        }

        return current;
    }
}

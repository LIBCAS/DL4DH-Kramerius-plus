package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
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

import java.util.Map;
import java.util.TreeMap;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class ExportItemsEnqueuingTasklet implements Tasklet {

    private final ExportRequest exportRequest;

    private final JobEnqueueService jobEnqueueService;

    @Autowired
    public ExportItemsEnqueuingTasklet(@Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                           ExportRequestStore exportRequestStore,
                                           JobEnqueueService jobEnqueueService) {
        this.exportRequest = exportRequestStore.find(exportRequestId);
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
            Map<Long, Export> children = new TreeMap<>(current.getChildren());
            if (children.values().isEmpty()) {
                isMaxDepth = true;
            } else {
                current = children.values().iterator().next(); // get first child
            }
        }

        return current;
    }
}

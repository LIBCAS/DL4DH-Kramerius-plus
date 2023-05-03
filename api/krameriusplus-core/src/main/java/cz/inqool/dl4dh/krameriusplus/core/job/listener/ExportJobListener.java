package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.core.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@RequiredArgsConstructor
@Component
public class ExportJobListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(EXPORT_ALTO, EXPORT_CSV, EXPORT_JSON, EXPORT_TEI, EXPORT_TEXT);

    private final ExportStore exportStore;

    private final ExportRequestStore exportRequestStore;

    private final JobEnqueueService jobEnqueueService;

    private final KrameriusJobInstanceStore jobInstanceStore;

    @Override
    public void beforeJob(String jobInstanceId) {
        // do nothing
    }

    @Override
    @Transactional
    public void afterJob(String jobInstanceId) {
        // Export jobs from one ExportRequestItem must run in sequence,
        // because otherwise it would be hard to determine, when to run
        // the MergeJob. There would also be some other issues with
        // includeChildExportStep, when parent export can finish before
        // all of it's child exports finish.
        KrameriusJobInstance jobInstance = jobInstanceStore.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));

        Export export = exportStore.findByExportJob(jobInstance);
        updateExportState(export);
        // TODO: propagate state to parent Exports and Request

        Export root = findRoot(export);
        List<Export> exportListInPostOrder = buildPostOrderList(root);

        Export nextExport = null;
        Iterator<Export> postOrderIterator = exportListInPostOrder.iterator();
        while (postOrderIterator.hasNext()) {
            Export current = postOrderIterator.next();

            if (current.equals(export) && postOrderIterator.hasNext()) {
                nextExport = postOrderIterator.next();
                break;
            }
        }

        if (nextExport != null) {
            jobEnqueueService.enqueue(nextExport.getExportJob());
        } else {
            ExportRequest exportRequest = exportRequestStore.findByRootExport(root);
            if (exportRequest.getItems().stream().allMatch(exportRequestItem ->
                    exportRequestItem.getRootExport().getExportJob().getExecutionStatus().isFinished())) {
                jobEnqueueService.enqueue(exportRequestStore.findMergeJob(root));
            }
        }
    }

    private void updateExportState(Export export) {
        // absence of fileref means failure in job
        if (export.getFileRef() == null) {
            export.setState(ExportState.FAILED);
        }
        // if fileref exists check if it's complete and set to successful accordingly
        else {
            export.setState(!export.getState().isIncomplete() ?
                    ExportState.SUCCESSFUL :  export.getState());
        }

        exportStore.save(export);
    }

    private List<Export> buildPostOrderList(Export root) {
        List<Export> result = new ArrayList<>();
        buildPostOrderList(root, result);

        return result;
    }

    private void buildPostOrderList(Export root, List<Export> resultList) {
        for (Export export : root.getChildrenList()) {
            buildPostOrderList(export, resultList);
        }

        resultList.add(root);
    }

    private Export findRoot(Export export) {
        Export current = export;
        while (current.getParent() != null) {
            current = current.getParent();
        }

        return current;
    }

    @Override
    public boolean supports(KrameriusJobInstance jobInstance) {
        return SUPPORTED_TYPES.contains(jobInstance.getJobType());
    }
}

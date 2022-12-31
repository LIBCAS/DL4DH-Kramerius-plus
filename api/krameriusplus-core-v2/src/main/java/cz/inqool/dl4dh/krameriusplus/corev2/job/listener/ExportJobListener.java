package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItemStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@Component
public class ExportJobListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(EXPORT_ALTO, EXPORT_CSV, EXPORT_JSON, EXPORT_TEI, EXPORT_TEXT);

    private ExportStore exportStore;

    private ExportRequestStore exportRequestStore;

    private JobEnqueueService jobEnqueueService;
    private final ExportRequestItemStore exportRequestItemStore;
    private final PageStore pageStore;

    public ExportJobListener(ExportRequestItemStore exportRequestItemStore,
                             PageStore pageStore) {
        this.exportRequestItemStore = exportRequestItemStore;
        this.pageStore = pageStore;
    }

    @Override
    public void afterJob(KrameriusJobInstance jobInstance) {
        // Export jobs from one ExportRequestItem must run in sequence,
        // because otherwise it would be hard to determine, when to run
        // the MergeJob. There would also be some other issues with
        // includeChildExportStep, when parent export can finish before
        // all of it's child exports finish.
        Export export = exportStore.findByExportJob(jobInstance);
        updateExportState(export);

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
                    exportRequestItem.getRootExport().getExportJob().getExecutionStatus().finished())) {
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

    @Autowired
    public void setExportStore(ExportStore exportStore) {
        this.exportStore = exportStore;
    }

    @Autowired
    public void setJobEnqueueService(JobEnqueueService jobEnqueueService) {
        this.jobEnqueueService = jobEnqueueService;
    }

    @Autowired
    public void setExportRequestStore(ExportRequestStore exportRequestStore) {
        this.exportRequestStore = exportRequestStore;
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.jms.JobEnqueueService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@Component
public class ExportJobListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(EXPORT_ALTO, EXPORT_CSV, EXPORT_JSON, EXPORT_TEI, EXPORT_TEXT);

    private ExportStore exportStore;

    private ExportRequestStore exportRequestStore;

    private JobEnqueueService jobEnqueueService;

    @Override
    public void afterJob(KrameriusJobInstance jobInstance) {
        // Export jobs from one ExportRequestItem must run in sequence,
        // because otherwise it would be hard to determine, when to run
        // the MergeJob. There would also be some other issues with
        // includeChildExportStep, when parent export can finish before
        // all of it's child exports finish.
        Export export = exportStore.findByExportJob(jobInstance);

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
            // TODO create new instance of merge job every time
            jobEnqueueService.enqueue(exportRequestStore.findMergeJob(root));
        }
    }

    private List<Export> buildPostOrderList(Export root) {
        List<Export> result = new ArrayList<>();
        Deque<Export> stack = new LinkedList<>();
        stack.add(root);

        while (!stack.isEmpty()) {
            root = stack.pop();
            result.add(root);
            stack.addAll(root.getChildrenList());
        }

        Collections.reverse(result);
        return result;
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

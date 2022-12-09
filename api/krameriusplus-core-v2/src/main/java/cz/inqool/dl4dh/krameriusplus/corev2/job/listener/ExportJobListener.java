package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.*;

@Component
public class ExportJobListener implements KrameriusJobListener {

    private final List<KrameriusJobType> SUPPORTED_TYPES = List.of(EXPORT_ALTO, EXPORT_CSV, EXPORT_JSON, EXPORT_TEI, EXPORT_TEXT);

    @Override
    public void afterJob(KrameriusJobInstance jobInstance) {
        // TODO: Run next export in PostOrder or if no next, run mergeJob
        // 1. Export by exportJob
        // 2. Find next in **PostOrder**
        // 3. If next found, enqueue, check if all items are FINISHED
        // 3.5 If yes, run merge job
        // DANGER!!! This block probably needs to be synchronized in case multiple items finish at the same time
        // and none of them will call mergeJob
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public boolean supports(KrameriusJobInstance jobInstance) {
        return SUPPORTED_TYPES.contains(jobInstance.getJobType());
    }
}

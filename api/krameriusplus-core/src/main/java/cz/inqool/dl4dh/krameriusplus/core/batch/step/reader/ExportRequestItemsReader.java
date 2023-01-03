package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
@StepScope
public class ExportRequestItemsReader implements ItemReader<ExportRequestItem> {

    private final Deque<ExportRequestItem> exportRequestItems;

    public ExportRequestItemsReader(@Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                    ExportRequestStore exportRequestStore) {
        this.exportRequestItems = new LinkedList<>(exportRequestStore.findById(exportRequestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, exportRequestId))
                .getItems());
    }

    @Override
    public ExportRequestItem read() throws Exception {
        if (exportRequestItems.isEmpty()) {
            return null;
        }

        return exportRequestItems.pop();
    }
}

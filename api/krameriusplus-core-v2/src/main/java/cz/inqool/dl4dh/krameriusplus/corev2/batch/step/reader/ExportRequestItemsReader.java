package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class ExportRequestItemsReader implements ItemReader<ExportRequestItem> {

    private final Deque<ExportRequestItem> exportRequestItems;

    public ExportRequestItemsReader(@Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                    ExportRequestStore exportRequestStore) {
        this.exportRequestItems = new LinkedList<>(exportRequestStore.find(exportRequestId).getItems());
    }

    @Override
    public ExportRequestItem read() throws Exception {
        if (exportRequestItems.isEmpty()) {
            return null;
        }

        return exportRequestItems.pop();
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.request.export.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItemStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportWriter implements ItemWriter<ExportRequestItem> {

    private ExportStore exportStore;

    private ExportRequestItemStore exportRequestItemStore;

    @Override
    public void write(List<? extends ExportRequestItem> items) {
        for (ExportRequestItem item : items) {
            item.setRootExport(exportStore.save(item.getRootExport()));

            exportRequestItemStore.save(item);
        }
    }

    @Autowired
    public void setExportStore(ExportStore exportStore) {
        this.exportStore = exportStore;
    }

    @Autowired
    public void setExportRequestItemStore(ExportRequestItemStore exportRequestItemStore) {
        this.exportRequestItemStore = exportRequestItemStore;
    }
}

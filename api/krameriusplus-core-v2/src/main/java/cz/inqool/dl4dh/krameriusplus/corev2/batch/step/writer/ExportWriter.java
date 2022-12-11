package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportWriter implements ItemWriter<Export> {

    private ExportStore exportStore;

    @Override
    public void write(List<? extends Export> items) {
        items.forEach(this::saveExportTree);
    }

    private void saveExportTree(Export export) {
        exportStore.create(export);

        for (Export child : export.getChildren().values()) {
            saveExportTree(child);
        }
    }

    @Autowired
    public void setExportStore(ExportStore exportStore) {
        this.exportStore = exportStore;
    }
}

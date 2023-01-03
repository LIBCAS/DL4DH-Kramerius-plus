package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItemStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class ExportItemWriter implements ItemWriter<ExportRequestItem> {

    private ExportRequestItemStore requestItemStore;

    @Override
    public void write(List<? extends ExportRequestItem> items) {
        requestItemStore.saveAll(items);
    }

    @Autowired
    public void setRequestItemStore(ExportRequestItemStore requestItemStore) {
        this.requestItemStore = requestItemStore;
    }
}

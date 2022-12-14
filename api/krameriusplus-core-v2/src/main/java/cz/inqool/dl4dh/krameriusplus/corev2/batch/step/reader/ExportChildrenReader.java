package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_ID;

@Component
@StepScope
public class ExportChildrenReader implements ItemReader<Export> {

    private final Deque<Export> children;

    public ExportChildrenReader(@Value("#{jobParameters['" + EXPORT_ID + "']}") String exportId,
                                ExportStore exportStore) {
        Export root = exportStore.findById(exportId)
                .orElseThrow(() -> new MissingObjectException(Export.class, exportId));

        this.children = new LinkedList<>(root.getChildrenList());
    }

    @Override
    public Export read() {
        if (children.isEmpty()) {
            return null;
        }

        return children.pop();
    }
}

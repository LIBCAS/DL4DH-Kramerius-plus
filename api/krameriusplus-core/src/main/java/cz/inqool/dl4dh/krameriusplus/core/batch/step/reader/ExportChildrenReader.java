package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
@StepScope
public class ExportChildrenReader implements ItemReader<Export> {

    private final Deque<Export> children;

    public ExportChildrenReader(@Value("#{jobParameters['" + JobParameterKey.EXPORT_ID + "']}") String exportId,
                                ExportStore exportStore) {
        Utils.notNull(exportId, () -> new IllegalStateException("ExportId not in jobParameters"));

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

package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.DigitalObjectProvider;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ExportItemCreatingProcessor implements ItemProcessor<String, ExportRequestItem> {

    private Long counter = 0L;

    private final ExportRequest exportRequest;

    private final DigitalObjectProvider digitalObjectProvider;

    public ExportItemCreatingProcessor(@Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                       ExportRequestStore exportRequestStore,
                                       DigitalObjectProvider digitalObjectProvider) {
        this.digitalObjectProvider = digitalObjectProvider;
        this.exportRequest = exportRequestStore.findById(exportRequestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, exportRequestId));
    }

    @Override
    public ExportRequestItem process(String item) throws Exception {
        Publication publication = digitalObjectProvider.find(item);

        ExportRequestItem requestItem = new ExportRequestItem();
        requestItem.setPublicationId(publication.getId());
        requestItem.setPublicationTitle(publication.getTitle());
        requestItem.setModel(publication.getModel());
        requestItem.setExportRequest(exportRequest);
        requestItem.setOrder(counter++);

        return requestItem;
    }
}

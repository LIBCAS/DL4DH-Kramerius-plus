package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.PublicationProvider;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class ExportItemCreatingProcessor implements ItemProcessor<String, ExportRequestItem> {

    private Long counter = 0L;

    private final ExportRequest exportRequest;

    private final PublicationProvider publicationProvider;

    public ExportItemCreatingProcessor(@Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                       ExportRequestStore exportRequestStore,
                                       PublicationProvider publicationProvider) {
        this.publicationProvider = publicationProvider;
        this.exportRequest = exportRequestStore.findById(exportRequestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, exportRequestId));
    }

    @Override
    public ExportRequestItem process(String item) throws Exception {
        Publication publication = publicationProvider.find(item);

        ExportRequestItem requestItem = new ExportRequestItem();
        requestItem.setPublicationId(publication.getId());
        requestItem.setPublicationTitle(publication.getTitle());
        requestItem.setModel(publication.getModel());
        requestItem.setExportRequest(exportRequest);
        requestItem.setOrder(counter++);

        return requestItem;
    }
}

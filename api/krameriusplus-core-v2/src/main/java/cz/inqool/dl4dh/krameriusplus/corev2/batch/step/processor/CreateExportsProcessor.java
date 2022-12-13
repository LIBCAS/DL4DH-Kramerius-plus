package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.PublicationProvider;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class CreateExportsProcessor implements ItemProcessor<ExportRequestItem, Export> {

    private final ExportRequest exportRequest;

    private PublicationProvider publicationProvider;

    private KrameriusJobInstanceService jobInstanceService;

    @Autowired
    public CreateExportsProcessor(@Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                  ExportRequestStore exportRequestStore) {
        this.exportRequest = exportRequestStore.find(exportRequestId);
    }

    @Override
    public Export process(ExportRequestItem item) throws Exception {
        Publication publication = publicationProvider.find(item.getPublicationId());

        return createExport(publication, null, 0L);
    }

    /**
     * Creates export for given publication and recursively for every child publication
     * @param publication
     * @return
     */
    private Export createExport(Publication publication, Export parent, Long order) {
        Export export = new Export();
        export.setPublicationId(publication.getId());
        export.setPublicationTitle(publication.getTitle());
        export.setParent(parent);
        export.setFormat(exportRequest.getConfig().getExportFormat());
        export.setOrder(order);
        export.setExportJob(
                jobInstanceService.createJobInstance(
                        exportRequest.getConfig().getJobType(),
                        exportRequest.getConfig().toJobParametersWrapper()));

        List<Publication> children = publicationProvider.findChildren(publication.getId());
        Long childrenOrder = 0L;
        for (Publication child : children) {
            export.putChild(childrenOrder, createExport(child, export, childrenOrder));

            childrenOrder++;
        }

        return export;
    }

    @Autowired
    public void setPublicationProvider(PublicationProvider publicationProvider) {
        this.publicationProvider = publicationProvider;
    }

    @Autowired
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }
}

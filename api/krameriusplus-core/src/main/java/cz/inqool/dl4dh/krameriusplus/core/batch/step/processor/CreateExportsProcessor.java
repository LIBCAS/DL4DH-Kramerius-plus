package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.DigitalObjectProvider;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.ExportRequestItem;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class CreateExportsProcessor implements ItemProcessor<ExportRequestItem, ExportRequestItem> {

    private final ExportRequest exportRequest;

    private DigitalObjectProvider digitalObjectProvider;

    private KrameriusJobInstanceService jobInstanceService;

    @Autowired
    public CreateExportsProcessor(@Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}") String exportRequestId,
                                  ExportRequestStore exportRequestStore) {
        this.exportRequest = exportRequestStore.findById(exportRequestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, exportRequestId));
    }

    @Override
    public ExportRequestItem process(ExportRequestItem item) throws Exception {
        Publication publication = digitalObjectProvider.find(item.getPublicationId());

        item.setRootExport(createExport(publication, null, 0L));
        return item;
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
        export.setModel(publication.getModel());

        JobParametersMapWrapper jobParametersMapWrapper = exportRequest.getConfig().toJobParametersWrapper();
        jobParametersMapWrapper.putString(JobParameterKey.PUBLICATION_ID, publication.getId());
        jobParametersMapWrapper.putString(JobParameterKey.EXPORT_ID, export.getId());

        export.setExportJob(
                jobInstanceService.createJobInstance(
                        exportRequest.getConfig().getJobType(),
                        jobParametersMapWrapper));

        List<Publication> children = digitalObjectProvider.findChildren(publication.getId())
                .stream()
                .filter(Publication.class::isInstance)
                .map(Publication.class::cast)
                .collect(Collectors.toList());

        Long childrenOrder = 0L;
        for (Publication child : children) {
            export.addChild(childrenOrder, createExport(child, export, childrenOrder));

            childrenOrder++;
        }

        return export;
    }

    @Autowired
    public void setPublicationProvider(DigitalObjectProvider digitalObjectProvider) {
        this.digitalObjectProvider = digitalObjectProvider;
    }

    @Autowired
    public void setJobInstanceService(KrameriusJobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }
}

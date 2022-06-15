package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PublicationMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components.ExportPublicationJsonFileWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION_JSON;

@Component
public class ExportPublicationJsonStepFactory extends PublicationMongoFlowStepFactory {

    private final ExportPublicationJsonFileWriter writer;

    @Autowired
    public ExportPublicationJsonStepFactory(ExportPublicationJsonFileWriter writer) {
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PUBLICATION_JSON;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }
}

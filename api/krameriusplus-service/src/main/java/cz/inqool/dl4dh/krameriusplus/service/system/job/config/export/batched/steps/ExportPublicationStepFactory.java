package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components.PublicationExportFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION;

@Component
public class ExportPublicationStepFactory extends AbstractStepFactory {

    private final PublicationMongoReader reader;

    private final PublicationExportFileWriter writer;

    @Autowired
    public ExportPublicationStepFactory(PublicationMongoReader reader, PublicationExportFileWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(EXPORT_PUBLICATION)
                .<Publication, Publication> chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }
}

package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components.ExportPublicationJsonFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION_JSON;

@Component
public class ExportPublicationJsonStepFactory extends AbstractStepFactory {

    private final ExportPublicationJsonFileWriter writer;

    private final PathResolvingProcessor processor;

    private final PublicationMongoReader reader;

    @Autowired
    public ExportPublicationJsonStepFactory(ExportPublicationJsonFileWriter writer,
                                            PathResolvingProcessor processor,
                                            PublicationMongoReader reader) {
        this.writer = writer;
        this.processor = processor;
        this.reader = reader;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PUBLICATION_JSON;
    }


    @Override
    public Step build() {
        return getBuilder()
                .<Publication, DigitalObjectWithPathDto>chunk(1)
                .reader(reader)
                .writer(writer)
                .processor(processor)
                .listener(processor)
                .build();
    }
}

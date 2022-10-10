package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components.TeiExportPublicationFilterer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components.TeiPublicationExporter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_TEI;

@Component
public class ExportTeiStepFactory extends AbstractStepFactory {

    private final DownloadDigitalObjectReader reader;

    private final TeiExportPublicationFilterer processor;

    private final TeiPublicationExporter writer;

    @Autowired
    public ExportTeiStepFactory(DownloadDigitalObjectReader reader,
                                TeiExportPublicationFilterer processor,
                                TeiPublicationExporter writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<DigitalObject, Publication>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_TEI;
    }
}

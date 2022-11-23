package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.TeiExportPublicationFiltererProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.TeiPublicationWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_TEI;

@Component
public class ExportTeiStepFactory extends FlowStepFactory<DigitalObject, Publication> {

    private final DownloadDigitalObjectReader reader;

    private final TeiExportPublicationFiltererProcessor processor;

    private final TeiPublicationWriter writer;

    @Autowired
    public ExportTeiStepFactory(DownloadDigitalObjectReader reader,
                                TeiExportPublicationFiltererProcessor processor,
                                TeiPublicationWriter writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Bean(EXPORT_TEI)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_TEI;
    }

    @Override
    protected int getChunkSize() {
        return 10;
    }

    @Override
    protected ItemReader<? extends DigitalObject> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<? super Publication> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<? super DigitalObject, ? extends Publication> getItemProcessor() {
        return processor;
    }
}

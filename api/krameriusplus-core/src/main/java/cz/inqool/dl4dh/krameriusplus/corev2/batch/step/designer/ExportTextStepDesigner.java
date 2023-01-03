package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.TextPageExportProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.KrameriusPageReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.DigitalObjectFileItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_TEXT_PAGES_STEP;

@Component
public class ExportTextStepDesigner extends AbstractStepDesigner {

    private KrameriusPageReader reader;

    private TextPageExportProcessor processor;

    private DigitalObjectFileItemWriter writer;

    @Bean(EXPORT_TEXT_PAGES_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, DigitalObjectExport>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_TEXT_PAGES_STEP;
    }

    @Autowired
    public void setReader(KrameriusPageReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(TextPageExportProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(DigitalObjectFileItemWriter writer) {
        this.writer = writer;
    }
}

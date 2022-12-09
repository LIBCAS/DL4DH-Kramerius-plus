package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.AltoPageExportProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.KrameriusPageReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.PageExport;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.PageFileItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_ALTO_STEP;

@Component
public class ExportAltoStepDesigner extends AbstractStepDesigner {

    private KrameriusPageReader reader;

    private AltoPageExportProcessor processor;

    private PageFileItemWriter writer;

    @Bean(EXPORT_ALTO_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, PageExport>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_ALTO_STEP;
    }

    @Autowired
    public void setReader(KrameriusPageReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(AltoPageExportProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(PageFileItemWriter writer) {
        this.writer = writer;
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.AltoTransformingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.KrameriusPageReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.AltoStringWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.AltoFileItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_ALTO_STEP;

@Component
public class ExportAltoStepDesigner extends AbstractStepDesigner {

    private KrameriusPageReader reader;

    private AltoTransformingProcessor processor;

    private AltoFileItemWriter writer;

    @Bean(EXPORT_ALTO_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, AltoStringWrappedPage>chunk(5)
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
    public void setProcessor(AltoTransformingProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(AltoFileItemWriter writer) {
        this.writer = writer;
    }
}

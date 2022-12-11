package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.CreateExportsProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.ExportRequestItemsReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.ExportWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.CREATE_EXPORTS_STEP;

@Component
public class CreateExportsStepDesigner extends AbstractStepDesigner {

    private ExportRequestItemsReader reader;

    private CreateExportsProcessor processor;

    private ExportWriter writer;

    @Bean(CREATE_EXPORTS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<ExportRequestItem, Export>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_EXPORTS_STEP;
    }

    @Autowired
    public void setReader(ExportRequestItemsReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(CreateExportsProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(ExportWriter writer) {
        this.writer = writer;
    }
}

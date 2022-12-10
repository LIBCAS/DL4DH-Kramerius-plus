package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.ExportItemCreatingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.ExportRequestReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.ExportItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.ExportRequestItem;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.CREATE_EXPORT_ITEMS_STEP;

@Component
public class CreateExportItemsStepDesigner extends AbstractStepDesigner {

    private ExportRequestReader reader;

    private ExportItemCreatingProcessor processor;

    private ExportItemWriter writer;

    @Bean(CREATE_EXPORT_ITEMS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<String, ExportRequestItem> chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_EXPORT_ITEMS_STEP;
    }

    @Autowired
    public void setReader(ExportRequestReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(ExportItemCreatingProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(ExportItemWriter writer) {
        this.writer = writer;
    }
}

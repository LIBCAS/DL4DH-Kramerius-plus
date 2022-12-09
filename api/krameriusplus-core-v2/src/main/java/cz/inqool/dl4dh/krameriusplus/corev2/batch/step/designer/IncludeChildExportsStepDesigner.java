package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.IncludeChildExportsProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.ExportChildrenReader;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.INCLUDE_CHILD_EXPORTS_STEP;

@Component
public class IncludeChildExportsStepDesigner extends AbstractStepDesigner {

    private ExportChildrenReader reader;

    private IncludeChildExportsProcessor processor;

    @Bean(INCLUDE_CHILD_EXPORTS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Export, Export> chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(items -> {}) // no-op writer because ItemWriter is mandatory
                .build();
    }

    @Override
    protected String getStepName() {
        return INCLUDE_CHILD_EXPORTS_STEP;
    }

    @Autowired
    public void setReader(ExportChildrenReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(IncludeChildExportsProcessor processor) {
        this.processor = processor;
    }
}

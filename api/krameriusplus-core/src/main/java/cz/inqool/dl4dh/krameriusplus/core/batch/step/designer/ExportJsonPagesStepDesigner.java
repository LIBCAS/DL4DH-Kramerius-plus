package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.processor.JsonPageExportProcessor;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.writer.DigitalObjectFileItemWriter;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.EXPORT_JSON_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER_W_TOKENS;

@Component
public class ExportJsonPagesStepDesigner extends AbstractStepDesigner {

    private MongoItemReader<Page> reader;

    private JsonPageExportProcessor processor;

    private DigitalObjectFileItemWriter writer;

    @Bean(EXPORT_JSON_PAGES_STEP)
    @Override
    public Step build() {
        return getStepBuilder().<Page, DigitalObjectExport>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_JSON_PAGES_STEP;
    }

    @Autowired
    public void setReader(@Qualifier(PAGE_MONGO_READER_W_TOKENS) MongoItemReader<Page> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(JsonPageExportProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(DigitalObjectFileItemWriter writer) {
        this.writer = writer;
    }
}

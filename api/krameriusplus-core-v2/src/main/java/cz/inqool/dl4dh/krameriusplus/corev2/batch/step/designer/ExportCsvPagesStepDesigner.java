package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.CsvExportPageProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.DigitalObjectFileItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_CSV_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER_W_TOKENS;

@Component
public class ExportCsvPagesStepDesigner extends AbstractStepDesigner {

    private MongoItemReader<Page> reader;

    private CsvExportPageProcessor processor;

    private DigitalObjectFileItemWriter writer;

    @Bean(EXPORT_CSV_PAGES_STEP)
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
        return EXPORT_CSV_PAGES_STEP;
    }

    @Autowired
    public void setReader(@Qualifier(PAGE_MONGO_READER_W_TOKENS) MongoItemReader<Page> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(CsvExportPageProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(DigitalObjectFileItemWriter writer) {
        this.writer = writer;
    }
}

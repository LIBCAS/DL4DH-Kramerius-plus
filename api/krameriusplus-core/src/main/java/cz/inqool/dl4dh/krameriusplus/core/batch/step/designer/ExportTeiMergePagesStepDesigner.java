package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.processor.ExportTeiMergePagesProcessor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.EXPORT_TEI_MERGE_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER_W_TOKENS;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.writer.ItemWritersConfig.KrameriusItemWriter.NO_OP_ITEM_WRITER;

@Component
public class ExportTeiMergePagesStepDesigner extends AbstractStepDesigner {

    private MongoItemReader<Page> reader;

    private ExportTeiMergePagesProcessor processor;

    private ItemWriter<Object> writer;

    @Bean(EXPORT_TEI_MERGE_PAGES_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_TEI_MERGE_PAGES_STEP;
    }

    @Autowired
    public void setReader(@Qualifier(PAGE_MONGO_READER_W_TOKENS) MongoItemReader<Page> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(ExportTeiMergePagesProcessor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(@Qualifier(NO_OP_ITEM_WRITER) ItemWriter<Object> writer) {
        this.writer = writer;
    }
}

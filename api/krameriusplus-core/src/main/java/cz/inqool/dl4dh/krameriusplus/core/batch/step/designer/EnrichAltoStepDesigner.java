package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.PageLimitCheckingSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.listener.StepSkipListener;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.ENRICH_ALTO_STEP;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.processor.ItemProcessorsConfig.KrameriusProcessor.ENRICH_ALTO_COMPOSITE_PROCESSOR;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER_W_TOKENS;

@Configuration
public class EnrichAltoStepDesigner extends AbstractStepDesigner {

    private MongoItemReader<Page> reader;

    private CompositeItemProcessor<Page, Page> processor;

    private MongoItemWriter<Page> writer;

    private StepSkipListener skipListener;

    private PageLimitCheckingSkipPolicy skipPolicy;

    @Override
    protected String getStepName() {
        return ENRICH_ALTO_STEP;
    }

    @Bean(ENRICH_ALTO_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(skipListener)
                .build();
    }

    @Autowired
    public void setReader(@Qualifier(PAGE_MONGO_READER_W_TOKENS) MongoItemReader<Page> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(@Qualifier(ENRICH_ALTO_COMPOSITE_PROCESSOR) CompositeItemProcessor<Page, Page> processor) {
        this.processor = processor;
    }

    @Autowired
    public void setWriter(MongoItemWriter<Page> writer) {
        this.writer = writer;
    }

    @Autowired
    public void setSkipListener(StepSkipListener skipListener) {
        this.skipListener = skipListener;
    }

    @Autowired
    public void setSkipPolicy(PageLimitCheckingSkipPolicy skipPolicy) {
        this.skipPolicy = skipPolicy;
    }
}

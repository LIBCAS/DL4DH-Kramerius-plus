package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.PageLimitCheckingSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.listener.StepSkipListener;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_NDK_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.ItemProcessorsConfig.KrameriusProcessor.ENRICH_NDK_COMPOSITE_PROCESSOR;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.ItemReadersConfig.KrameriusReader.PAGE_MONGO_READER;

@Component
public class EnrichNdkStepDesigner extends AbstractStepDesigner {

    private MongoItemReader<Page> reader;

    private CompositeItemProcessor<Page, Page> processor;

    private MongoItemWriter<Page> writer;

    private StepSkipListener skipListener;

    private PageLimitCheckingSkipPolicy skipPolicy;

    @Override
    protected String getStepName() {
        return ENRICH_NDK_STEP;
    }

    @Bean(ENRICH_NDK_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .<Page, Page>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(skipListener)
                .build();
    }

    @Autowired
    public void setReader(@Qualifier(PAGE_MONGO_READER) MongoItemReader<Page> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setProcessor(@Qualifier(ENRICH_NDK_COMPOSITE_PROCESSOR) CompositeItemProcessor<Page, Page> processor) {
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

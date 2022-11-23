package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.CustomLimitCheckingSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener.ErrorPersistingSkipListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.PageMongoWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract base class for step factories, which read and write pages from MongoDB.
 */
public abstract class PageMongoFlowStepFactory extends FlowStepFactory<Page, Page> {

    protected PageMongoReader reader;

    protected PageMongoWriter writer;

    protected CustomLimitCheckingSkipPolicy skipPolicy;

    protected ErrorPersistingSkipListener skipListener;

    @Override
    public Step build() {
        return super.getBuilder()
                .<Page, Page>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(writeListener)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(skipListener)
                .build();
    }

    @Override
    protected ItemReader<Page> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<Page> getItemWriter() {
        return writer;
    }

    @Autowired
    public void setReader(PageMongoReader reader) {
        this.reader = reader;
    }

    @Autowired
    public void setWriter(PageMongoWriter writer) {
        this.writer = writer;
    }

    @Autowired
    public void setSkipPolicy(CustomLimitCheckingSkipPolicy skipPolicy) {
        this.skipPolicy = skipPolicy;
    }

    @Autowired
    public void setSkipListener(ErrorPersistingSkipListener skipListener) {
        this.skipListener = skipListener;
    }
}

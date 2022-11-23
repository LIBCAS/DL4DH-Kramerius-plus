package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.ErrorPersistingSkipPolicy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.PageMongoWriter;
import org.springframework.batch.core.ItemProcessListener;
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

    protected ErrorPersistingSkipPolicy errorPersistingSkipPolicy;

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
    public void setErrorPersistingSkipPolicy(ErrorPersistingSkipPolicy errorPersistingSkipPolicy) {
        this.errorPersistingSkipPolicy = errorPersistingSkipPolicy;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<Page, Page>chunk(1)
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(writeListener)
                .faultTolerant()
                .skipPolicy(errorPersistingSkipPolicy)
                .listener((ItemProcessListener<? super Page, ? super Page>) errorPersistingSkipPolicy)
                .build();
    }
}

package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.writer.PageMongoWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract base class for step factories, which read and write pages from MongoDB.
 */
public abstract class PageMongoPersistentStepFactory extends PersistentStepFactory<Page, Page> {

    protected PageMongoReader reader;

    protected PageMongoWriter writer;

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
}

package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DatedObjectFieldGenerator;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DomainObject;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatedObjectWriteListener implements ItemWriteListener<DomainObject> {

    private final DatedObjectFieldGenerator generator;

    @Autowired
    public DatedObjectWriteListener(DatedObjectFieldGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void beforeWrite(List<? extends DomainObject> items) {
        items.forEach(item -> {
            if (item instanceof DatedObject) {
                generator.generateFields((DatedObject) item);
            }
        });
    }

    @Override
    public void afterWrite(List<? extends DomainObject> items) {
        // do nothing
    }

    @Override
    public void onWriteError(Exception exception, List<? extends DomainObject> items) {
        // do nothing
    }
}

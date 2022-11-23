package cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DatedObjectFieldGenerator;
import lombok.NonNull;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatedObjectWriteListener implements ItemWriteListener<Object> {

    private final DatedObjectFieldGenerator generator;

    @Autowired
    public DatedObjectWriteListener(DatedObjectFieldGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void beforeWrite(List<?> items) {
        items.forEach(item -> {
            if (item instanceof DatedObject) {
                generator.generateFields((DatedObject) item);
            }
        });
    }

    @Override
    public void afterWrite(@NonNull List<?> items) {
        // do nothing
    }

    @Override
    public void onWriteError(@NonNull Exception exception, @NonNull List<?> items) {
        // do nothing
    }
}

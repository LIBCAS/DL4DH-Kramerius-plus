package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.common;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DomainStore;
import lombok.NonNull;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DomainItemWriter<T extends DomainObject> implements ItemWriter<T> {

    private final DomainStore<T, ?> store;

    public DomainItemWriter(DomainStore<T, ?> store) {
        this.store = store;
    }

    @Override
    public void write(@NonNull List<? extends T> items) {
        store.create(items);
    }
}

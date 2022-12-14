package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import com.querydsl.core.types.dsl.EntityPathBase;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.Request;
import org.springframework.batch.item.ItemReader;

import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeMap;

public abstract class RequestPublicationIdReader<T extends Request, Q extends EntityPathBase<T>>
        implements ItemReader<String> {

    private Deque<String> publicationIds;

    protected abstract String getRequestId();

    protected abstract DomainStore<T, Q> getRequestStore();

    @Override
    public String read() {
        if (publicationIds == null) {
            T request = getRequestStore().findById(getRequestId())
                    .orElseThrow(() -> new MissingObjectException(getRequestStore().getType(), getRequestId()));
            publicationIds = new LinkedList<>(new TreeMap<>(request.getPublicationIds()).values());
        }

        return publicationIds.isEmpty() ? null : publicationIds.pop();
    }
}

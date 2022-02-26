package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PageStore extends DomainStore<Page> {

    @Autowired
    public PageStore(MongoOperations mongoOperations) {
        super(Page.class, mongoOperations);
    }

    @Override
    public List<Page> listAll(Params params) {
        List<Page> result = new ArrayList<>();


        List<Page> batch;
        params.setPageSize(20);
        int page = 0;

        do {
            params.setPage(page++);
            batch = mongoOperations.find(params.toQuery(), type);

            result.addAll(batch);
        } while (!batch.isEmpty());

        return result;
    }
}

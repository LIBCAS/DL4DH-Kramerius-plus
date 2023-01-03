package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageStore extends CrudRepository<Page, String>, CustomPageStore {
}

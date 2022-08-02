package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageStore extends CrudRepository<Page, String>, CustomPageStore {
}

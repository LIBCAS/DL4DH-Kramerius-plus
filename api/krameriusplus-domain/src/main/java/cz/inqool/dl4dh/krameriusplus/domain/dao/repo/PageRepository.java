package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface PageRepository extends MongoRepository<Page, String> {

    List<Page> findAllByParentIdOrderByIndexAsc(String parentId);

    List<Page> findByParentIdOrderByIndexAsc(String parentId, Pageable pageable);
}

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

    List<Page> findAllByParentIdOrderByPageIndexAsc(String parentId);

    List<Page> findByParentIdOrderByPageIndexAsc(String parentId, Pageable pageable);
}

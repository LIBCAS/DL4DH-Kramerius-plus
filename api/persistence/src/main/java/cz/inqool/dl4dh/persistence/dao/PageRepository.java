package cz.inqool.dl4dh.persistence.dao;

import cz.inqool.dl4dh.persistence.entity.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface PageRepository extends MongoRepository<Page, String> {

    List<Page> findAllByRootIdOrderByPageIndexAsc(String rootId);

    List<Page> findByRootIdOrderByPageIndexAsc(String rootId, Pageable pageable);
}

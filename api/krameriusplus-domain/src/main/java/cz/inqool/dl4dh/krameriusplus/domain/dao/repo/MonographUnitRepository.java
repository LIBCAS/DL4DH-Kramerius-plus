package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface MonographUnitRepository extends MongoRepository<MonographUnit, String> {

    List<MonographUnit> findAllByParentIdOrderByPartNumberAsc(String parentId);
}

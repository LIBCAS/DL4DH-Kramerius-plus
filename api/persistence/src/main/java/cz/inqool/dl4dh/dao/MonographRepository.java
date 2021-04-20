package cz.inqool.dl4dh.dao;

import cz.inqool.dl4dh.entity.Monograph;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface MonographRepository extends MongoRepository<Monograph, String> {
}

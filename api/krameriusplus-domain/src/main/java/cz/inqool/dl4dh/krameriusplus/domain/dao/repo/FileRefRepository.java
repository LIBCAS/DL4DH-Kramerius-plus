package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.FileRef;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Norbert Bodnar
 */
public interface FileRefRepository extends MongoRepository<FileRef, String> {
}

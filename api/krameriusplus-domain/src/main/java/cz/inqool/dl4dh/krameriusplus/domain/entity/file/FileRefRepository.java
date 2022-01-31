package cz.inqool.dl4dh.krameriusplus.domain.entity.file;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface FileRefRepository extends MongoRepository<FileRef, String> {
}

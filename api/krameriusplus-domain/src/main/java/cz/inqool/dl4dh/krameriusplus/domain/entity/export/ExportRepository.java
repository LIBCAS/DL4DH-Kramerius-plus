package cz.inqool.dl4dh.krameriusplus.domain.entity.export;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface ExportRepository extends MongoRepository<Export, String> {

    @Query("{deleted: {$exists: false}, created: {$lt: ?0}}")
    List<Export> findAllToDelete(Instant beforeThis);
}

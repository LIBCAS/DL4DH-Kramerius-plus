package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.export.Export;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Norbert Bodnar
 */
public interface ExportRepository extends MongoRepository<Export, String> {
}

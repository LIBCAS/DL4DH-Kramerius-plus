package cz.inqool.dl4dh.krameriusplus.domain.dao;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Norbert Bodnar
 */
public interface PublicationRepository extends MongoRepository<Publication, String> {
}

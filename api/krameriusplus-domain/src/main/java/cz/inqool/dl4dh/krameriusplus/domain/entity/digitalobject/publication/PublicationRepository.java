package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface PublicationRepository extends MongoRepository<Publication, String>, PublicationCustomRepository {

    @Query(value = "{}", fields = "{title: 1, date: 1, issueNumber: 1, index: 1, partNumber: 1, _class: 1, volumeYear: 1}")
    List<Publication> listLight();
}

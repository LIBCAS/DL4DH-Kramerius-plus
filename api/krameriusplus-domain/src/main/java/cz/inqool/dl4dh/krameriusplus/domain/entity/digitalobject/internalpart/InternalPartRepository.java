package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalPartRepository extends MongoRepository<InternalPart, String>, InternalPartCustomRepository {
}

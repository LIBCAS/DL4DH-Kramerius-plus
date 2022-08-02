package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalPartStore extends CrudRepository<InternalPart, String> {
}

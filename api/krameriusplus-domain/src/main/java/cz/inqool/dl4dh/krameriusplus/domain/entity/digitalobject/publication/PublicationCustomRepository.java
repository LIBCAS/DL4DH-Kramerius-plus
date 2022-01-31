package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.domain.repo.CustomRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PublicationCustomRepository extends CustomRepository<Publication> {
}

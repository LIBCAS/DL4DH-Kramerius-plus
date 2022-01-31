package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.KrameriusModel.PERIODICAL;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(PERIODICAL)
@Document(collection = "publications")
public class Periodical extends Publication {
}

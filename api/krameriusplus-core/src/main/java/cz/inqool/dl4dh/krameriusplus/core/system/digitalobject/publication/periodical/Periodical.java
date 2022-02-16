package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(KrameriusModel.PERIODICAL)
@Document(collection = "publications")
public class Periodical extends Publication {
}

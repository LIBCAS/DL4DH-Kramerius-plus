package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public class Periodical extends Publication {
}

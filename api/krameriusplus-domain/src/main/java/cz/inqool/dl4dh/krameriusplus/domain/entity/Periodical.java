package cz.inqool.dl4dh.krameriusplus.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "periodicals")
public class Periodical extends Publication {
}

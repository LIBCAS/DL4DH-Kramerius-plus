package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a MonographUnit. MonographUnits must contain pages directly as children
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public class MonographUnit extends Publication {

    private String partNumber;

    private String partTitle;

    private String donator;

    private String parentId;

    @Transient
    private List<Page> pages = new ArrayList<>();
}

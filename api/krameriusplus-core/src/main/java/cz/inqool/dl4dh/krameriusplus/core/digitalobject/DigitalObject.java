package cz.inqool.dl4dh.krameriusplus.core.digitalobject;

import cz.inqool.dl4dh.krameriusplus.api.publication.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class DigitalObject extends DomainDocument {

    private String parentId;

    @Indexed
    private Integer index;

    private String rootId;

    private String title;

    private List<DigitalObjectContext> context = new ArrayList<>();

    public abstract KrameriusModel getModel();
}

package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import cz.inqool.dl4dh.krameriusplus.api.domain.DomainDocumentDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.DigitalObjectContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class DigitalObjectDto extends DomainDocumentDto {

    private String parentId;

    private Integer index;

    private String rootId;

    private String title;

    private List<DigitalObjectContext> context = new ArrayList<>();
}

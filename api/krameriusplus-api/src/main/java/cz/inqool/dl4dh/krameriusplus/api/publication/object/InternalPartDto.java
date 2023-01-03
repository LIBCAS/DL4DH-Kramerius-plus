package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InternalPartDto extends PublicationDto {

    private String pageRange;

    private String partTitle;

    private String partType;

    private String pageNumber;
}

package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MonographUnitDto extends PublicationDto {

    private String partNumber;

    private String partTitle;
}

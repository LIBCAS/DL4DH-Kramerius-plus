package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.MONOGRAPH_UNIT;

@Getter
@Setter
@JsonTypeName(MONOGRAPH_UNIT)
public class MonographUnitDto extends PublicationDto {

    private String partNumber;

    private String partTitle;

    private String rootPid;
}

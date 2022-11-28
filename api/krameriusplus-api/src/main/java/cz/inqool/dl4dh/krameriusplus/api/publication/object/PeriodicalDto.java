package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.PERIODICAL;

@Getter
@Setter
@JsonTypeName(PERIODICAL)
public class PeriodicalDto extends PublicationDto {
}

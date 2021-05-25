package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusModelAware;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalDto extends PublicationDto implements KrameriusModelAware {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }
}

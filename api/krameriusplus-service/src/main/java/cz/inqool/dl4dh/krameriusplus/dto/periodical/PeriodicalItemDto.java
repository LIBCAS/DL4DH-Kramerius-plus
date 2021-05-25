package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL_ITEM;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalItemDto extends PublicationDto<PeriodicalItem> {

    private String date;

    private String issueNumber;

    private String partNumber;

    private String rootPid;

    private String rootTitle;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        date = (String) details.get("date");
        issueNumber = (String) details.get("issueNumber");
        partNumber = (String) details.get("partNumber");
    }

    private final KrameriusModel model = PERIODICAL_ITEM;

    @Override
    public PeriodicalItem toEntity() {
        return null;
    }
}

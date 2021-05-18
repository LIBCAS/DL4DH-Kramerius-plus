package cz.inqool.dl4dh.krameriusplus.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH_UNIT;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class KrameriusMonographUnitDto extends KrameriusPublicationDto implements KrameriusModelAware {

    private String partNumber;

    private String partTitle;

    private String donator;

    @JsonProperty("root_pid")
    private String rootPid;

    private List<KrameriusPageDto> pages = new ArrayList<>();

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        partNumber = (String) details.get("partNumber");
        partTitle = ((String) details.get("title"));
    }

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH_UNIT;
    }

    public MonographUnit toEntity() {
        MonographUnit monographUnit = super.toEntity(new MonographUnit());
        monographUnit.setPartNumber(partNumber);
        monographUnit.setPartTitle(partTitle);
        monographUnit.setDonator(donator);
        monographUnit.setRootPid(rootPid);

        return monographUnit;
    }
}

package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH_UNIT;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographUnitDto extends PublicationDto<MonographUnit> implements ParentAware {

    private String partNumber;

    private String partTitle;

    private String donator;

    private String parentId;

    private Integer index;

    /**
     * Is ignored
     */
    @JsonProperty("root_pid")
    private String rootPid;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        partNumber = (String) details.get("partNumber");
        partTitle = ((String) details.get("title"));
    }

    @Override
    public KrameriusModel getModel() {
        return MONOGRAPH_UNIT;
    }

    @Override
    public MonographUnit toEntity() {
        MonographUnit monographUnit = super.toEntity(new MonographUnit());
        monographUnit.setPartNumber(partNumber);
        monographUnit.setPartTitle(partTitle);
        monographUnit.setDonator(donator);
        monographUnit.setParentId(parentId);
        monographUnit.setIndex(index);

        return monographUnit;
    }

    @Override
    public MonographUnit accept(KrameriusPublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}

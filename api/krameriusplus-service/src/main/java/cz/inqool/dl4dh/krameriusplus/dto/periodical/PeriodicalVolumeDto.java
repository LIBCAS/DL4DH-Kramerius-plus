package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalVolumeDto extends PublicationDto<PeriodicalVolume> {

    private String volumeNumber;

    private String volumeYear;

    private String rootTitle;

    private String rootPid;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        volumeNumber = (String) details.get("volumeNumber");
        volumeYear = ((String) details.get("year"));
    }

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL_VOLUME;
    }

    @Override
    public PeriodicalVolume toEntity() {
        PeriodicalVolume entity = super.toEntity(new PeriodicalVolume());
        entity.setVolumeNumber(volumeNumber);
        entity.setVolumeYear(volumeYear);
        entity.setParentId(rootPid);

        return entity;
    }

    @Override
    public PeriodicalVolume accept(KrameriusPublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}

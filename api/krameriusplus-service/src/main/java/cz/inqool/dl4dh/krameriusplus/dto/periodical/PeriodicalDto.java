package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusModelAware;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalDto extends PublicationDto<Periodical> {

    private List<PeriodicalVolumeDto> volumes;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }

    @Override
    public Periodical toEntity() {
        Periodical entity = super.toEntity(new Periodical());
        entity.setPeriodicalVolumes(volumes
                .stream()
                .map(PeriodicalVolumeDto::toEntity)
                .collect(Collectors.toList()));

        return entity;
    }
}

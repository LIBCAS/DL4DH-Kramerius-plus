package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithUnits;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithUnitsDto extends MonographDto<MonographWithUnits> {

    private List<MonographUnitDto> monographUnits;

    @Override
    public MonographWithUnits toEntity() {
        MonographWithUnits entity = super.toEntity(new MonographWithUnits());
        entity.setMonographUnits(monographUnits
                .stream()
                .map(MonographUnitDto::toEntity)
                .collect(Collectors.toList()));

        return entity;
    }
}

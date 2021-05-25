package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithUnitsDto extends MonographDto {

    private List<MonographUnitDto> monographUnits;

    @Override
    public Monograph toEntity() {
        return null;
    }
}

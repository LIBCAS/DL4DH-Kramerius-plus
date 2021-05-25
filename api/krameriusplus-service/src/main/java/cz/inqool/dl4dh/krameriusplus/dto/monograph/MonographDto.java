package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographDto extends PublicationDto<Monograph> {

    private List<PageDto> pages;

    private List<MonographUnitDto> monographUnits;

    private String donator;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.MONOGRAPH;
    }

    public Monograph toEntity() {
        Monograph monograph = super.toEntity(new Monograph());
        monograph.setDonator(donator);

        return monograph;
    }
}

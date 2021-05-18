package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPublicationDto;
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
public class KrameriusMonographDto extends KrameriusPublicationDto {

    private List<KrameriusPageDto> pages;

    private List<KrameriusMonographUnitDto> monographUnits;

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

package cz.inqool.dl4dh.krameriusplus.domain.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.NO_CHILDREN;

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

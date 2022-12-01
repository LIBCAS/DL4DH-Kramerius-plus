package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.MonographCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.MonographUnitCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other.InternalPartCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other.SupplementCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalItemCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalVolumeCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.InternalPart;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Supplement;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.PeriodicalVolume;

/**
 * Visitor pattern base class
 */
public interface DigitalObjectMapperVisitor {

    Monograph fromCreateDto(MonographCreateDto createDto);

    MonographUnit fromCreateDto(MonographUnitCreateDto createDto);

    Periodical fromCreateDto(PeriodicalCreateDto createDto);

    PeriodicalVolume fromCreateDto(PeriodicalVolumeCreateDto createDto);

    PeriodicalItem fromCreateDto(PeriodicalItemCreateDto createDto);

    Page fromCreateDto(PageCreateDto createDto);

    Supplement fromCreateDto(SupplementCreateDto createDto);

    InternalPart fromCreateDto(InternalPartCreateDto createDto);
}

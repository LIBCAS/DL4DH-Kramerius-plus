package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.api.publication.object.*;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph.MonographCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph.MonographUnitCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other.InternalPartCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other.SupplementCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalItemCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalVolumeCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.InternalPart;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Supplement;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalVolume;

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

    MonographDto toDto(Monograph entity);

    MonographUnitDto toDto(MonographUnit entity);

    PeriodicalDto toDto(Periodical entity);

    PeriodicalVolumeDto toDto(PeriodicalVolume entity);

    PeriodicalItemDto toDto(PeriodicalItem entity);

    PageDto toDto(Page entity);

    SupplementDto toDto(Supplement entity);

    InternalPartDto toDto(InternalPart entity);
}

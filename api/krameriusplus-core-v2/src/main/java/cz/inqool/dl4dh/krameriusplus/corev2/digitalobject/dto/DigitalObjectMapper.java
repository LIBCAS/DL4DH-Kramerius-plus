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
import org.springframework.stereotype.Component;

@Component
public class DigitalObjectMapper implements DigitalObjectMapperVisitor {

    @Override
    public Monograph fromCreateDto(MonographCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public MonographUnit fromCreateDto(MonographUnitCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Periodical fromCreateDto(PeriodicalCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public PeriodicalVolume fromCreateDto(PeriodicalVolumeCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public PeriodicalItem fromCreateDto(PeriodicalItemCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Page fromCreateDto(PageCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Supplement fromCreateDto(SupplementCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public InternalPart fromCreateDto(InternalPartCreateDto createDto) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
        Periodical periodical = new Periodical();
        periodical.setId(createDto.getPid());
        periodical.setTitle(createDto.getTitle());
        periodical.setRootTitle(createDto.getRootTitle());
        periodical.setRootId(createDto.getRootPid());
        periodical.setPolicy(createDto.getPolicy());

        if (createDto.getContext().size() == 1) {
            periodical.setContext(createDto.getContext().get(0));
        } else {
            log.warn("Periodical {}: expected context size=1, actual={}", createDto.getPid(), createDto.getContext().size());
        }

        periodical.setCollections(createDto.getCollections());

        return periodical;
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

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
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
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
        Monograph monograph = new Monograph();
        mapPublicationProperties(monograph, createDto);

        return monograph;
    }

    @Override
    public MonographUnit fromCreateDto(MonographUnitCreateDto createDto) {
        MonographUnit monographUnit = new MonographUnit();
        mapPublicationProperties(monographUnit, createDto);
        monographUnit.setPartNumber(createDto.getDetails().getPartNumber());
        monographUnit.setPartTitle(createDto.getDetails().getTitle());

        return monographUnit;
    }

    @Override
    public Periodical fromCreateDto(PeriodicalCreateDto createDto) {
        Periodical periodical = new Periodical();
        mapPublicationProperties(periodical, createDto);

        return periodical;
    }

    @Override
    public PeriodicalVolume fromCreateDto(PeriodicalVolumeCreateDto createDto) {
        PeriodicalVolume periodicalVolume = new PeriodicalVolume();
        mapPublicationProperties(periodicalVolume, createDto);
        periodicalVolume.setVolumeNumber(createDto.getDetails().getVolumeNumber());
        periodicalVolume.setVolumeYear(createDto.getDetails().getYear());

        return periodicalVolume;
    }

    @Override
    public PeriodicalItem fromCreateDto(PeriodicalItemCreateDto createDto) {
        PeriodicalItem periodicalItem = new PeriodicalItem();
        mapPublicationProperties(periodicalItem, createDto);
        periodicalItem.setIssueNumber(createDto.getDetails().getIssueNumber());
        periodicalItem.setPartNumber(createDto.getDetails().getPartNumber());
        periodicalItem.setDate(createDto.getDetails().getDate());

        return periodicalItem;
    }

    @Override
    public Page fromCreateDto(PageCreateDto createDto) {
        Page page = new Page();
        mapPageProperties(page, createDto);

        return page;
    }

    @Override
    public Supplement fromCreateDto(SupplementCreateDto createDto) {
        Supplement supplement = new Supplement();
        mapPublicationProperties(supplement, createDto);

        return supplement;
    }

    @Override
    public InternalPart fromCreateDto(InternalPartCreateDto createDto) {
        InternalPart internalPart = new InternalPart();
        mapPageProperties(internalPart, createDto);

        return internalPart;
    }

    private void mapPageProperties(Page to, PageCreateDto from) {
        to.setId(from.getPid());
        to.setTitle(from.getTitle());
        to.setRootId(from.getRootPid());
        to.setPolicy(from.getPolicy());
        to.setPageType(from.getDetails().getType());
        to.setPageNumber(from.getDetails().getPageNumber());
    }

    private void mapPublicationProperties(Publication to, PublicationCreateDto from) {
        to.setId(from.getPid());
        to.setTitle(from.getTitle());
        to.setRootTitle(from.getRootTitle());
        to.setRootId(from.getRootPid());
        to.setPolicy(from.getPolicy());
        to.setCollections(from.getCollections());
        to.setPdf(from.getPdf() != null);

        if (from.getContext().size() == 1) {
            to.setContext(from.getContext().get(0));
        } else {
            log.warn("{} {}: expected context size=1, actual={}", to.getClass().getSimpleName(), from.getPid(), from.getContext().size());
        }
    }
}

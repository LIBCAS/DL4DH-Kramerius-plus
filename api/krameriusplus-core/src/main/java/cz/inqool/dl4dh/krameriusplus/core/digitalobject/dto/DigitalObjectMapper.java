package cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto;

import cz.inqool.dl4dh.krameriusplus.api.publication.object.*;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph.MonographCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.monograph.MonographUnitCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other.InternalPartCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.other.SupplementCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalItemCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.periodical.PeriodicalVolumeCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.InternalPart;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Supplement;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalVolume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class DigitalObjectMapper implements DigitalObjectMapperVisitor {

    private PageMapper pageMapper;

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
        supplement.setDate(createDto.getDetails().getDate());

        return supplement;
    }

    @Override
    public InternalPart fromCreateDto(InternalPartCreateDto createDto) {
        InternalPart internalPart = new InternalPart();
        mapPublicationProperties(internalPart, createDto);
        internalPart.setPartTitle(createDto.getTitle());
        internalPart.setPageNumber(createDto.getInternalPartDetails().getPageNumber());
        internalPart.setPageRange(createDto.getInternalPartDetails().getPageRange());
        internalPart.setPartType(createDto.getInternalPartDetails().getType());

        return internalPart;
    }

    @Override
    public MonographDto toDto(Monograph entity) {
        MonographDto dto = new MonographDto();
        mapPublicationDtoProperties(dto, entity);

        return dto;
    }


    @Override
    public MonographUnitDto toDto(MonographUnit entity) {
        MonographUnitDto dto = new MonographUnitDto();
        mapPublicationDtoProperties(dto, entity);
        dto.setPartTitle(entity.getPartTitle());
        dto.setPartNumber(entity.getPartNumber());

        return dto;
    }

    @Override
    public PeriodicalDto toDto(Periodical entity) {
        PeriodicalDto dto = new PeriodicalDto();
        mapPublicationDtoProperties(dto, entity);

        return dto;
    }

    @Override
    public PeriodicalVolumeDto toDto(PeriodicalVolume entity) {
        PeriodicalVolumeDto dto = new PeriodicalVolumeDto();
        mapPublicationDtoProperties(dto, entity);
        dto.setVolumeNumber(entity.getVolumeNumber());
        dto.setVolumeYear(entity.getVolumeYear());

        return dto;
    }

    @Override
    public PeriodicalItemDto toDto(PeriodicalItem entity) {
        PeriodicalItemDto dto = new PeriodicalItemDto();
        mapPublicationDtoProperties(dto, entity);
        dto.setDate(entity.getDate());
        dto.setIssueNumber(entity.getIssueNumber());
        dto.setPartNumber(entity.getPartNumber());

        return dto;
    }

    @Override
    public PageDto toDto(Page entity) {
        PageDto pageDto = new PageDto();
        mapDigitalObjectDtoProperties(pageDto, entity);
        pageDto.setTokens(entity.getTokens().stream().map(token -> pageMapper.toDto(token)).collect(Collectors.toList()));
        pageDto.setPolicy(entity.getPolicy());
        pageDto.setPageNumber(entity.getPageNumber());
        pageDto.setNameTagMetadata(pageMapper.toDto(entity.getNameTagMetadata()));
        pageDto.setMetsMetadata(entity.getMetsMetadata());

        return pageDto;
    }

    @Override
    public SupplementDto toDto(Supplement entity) {
        SupplementDto dto = new SupplementDto();
        mapPublicationDtoProperties(dto, entity);
        dto.setDate(entity.getDate());

        return dto;
    }

    @Override
    public InternalPartDto toDto(InternalPart entity) {
        InternalPartDto dto = new InternalPartDto();
        mapPublicationDtoProperties(dto, entity);

        dto.setPageRange(entity.getPageRange());
        dto.setPartTitle(entity.getPartTitle());
        dto.setPartType(entity.getPartType());
        dto.setPageNumber(entity.getPageNumber());

        return dto;
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

        if (!from.getContext().isEmpty()) {
            if (from.getContext().size() == 1) {
                to.setContext(from.getContext().get(0));
            } else {
                log.warn(String.format("%s %s: expected context size=1, actual=%s",
                        to.getClass().getSimpleName(), from.getPid(), from.getContext().size()));
            }
        }
    }

    private void mapPublicationDtoProperties(PublicationDto dto, Publication entity) {
        dto.setChildren(entity.getChildren().stream()
                .map(child -> child.accept(this))
                .collect(Collectors.toList()));
        dto.setPages(entity.getPages().stream()
                .map(page -> page.accept(this))
                .collect(Collectors.toList()));
        dto.setCollections(entity.getCollections());
        dto.setPolicy(entity.getPolicy());
        dto.setModsMetadata(entity.getModsMetadata());
        dto.setPublishInfo(entity.getPublishInfo());
        dto.setParadata(entity.getParadata());
        dto.setPdf(entity.isPdf());
        dto.setDonator(entity.getDonator());
        dto.setPageCount(entity.getPageCount());

        mapDigitalObjectDtoProperties(dto, entity);
    }

    private void mapDigitalObjectDtoProperties(DigitalObjectDto dto, DigitalObject entity) {
        dto.setParentId(entity.getParentId());
        dto.setIndex(entity.getIndex());
        dto.setRootId(entity.getRootId());
        dto.setTitle(entity.getTitle());
        dto.setModel(entity.getModel());

        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());

        dto.setId(entity.getId());
    }

    @Autowired
    public void setTokenMapper(PageMapper pageMapper) {
        this.pageMapper = pageMapper;
    }
}

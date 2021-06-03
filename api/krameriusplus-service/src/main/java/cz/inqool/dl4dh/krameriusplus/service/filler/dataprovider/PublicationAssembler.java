package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithPages;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithUnits;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;
import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalItemDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalVolumeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
public class PublicationAssembler implements PublicationAssemblerVisitor {

    private final DataProvider dataProvider;

    @Autowired
    public PublicationAssembler(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Monograph assemble(MonographDto monographDto) {
        List<DigitalObjectDto<DomainObject>> children = dataProvider.getDigitalObjectsForParent(monographDto.getPid());

        // children should never be emtpy or null;
        DigitalObjectDto<? extends DomainObject> firstChild = children.get(0);

        if (firstChild instanceof MonographUnitDto) {
            return getMonographWithUnits(monographDto, children);
        } else if (firstChild instanceof PageDto) {
            return getMonographWithPages(monographDto, children);
        } else {
            throw new IllegalStateException("Illegal type of children for monograph");
        }

    }

    private MonographWithUnits getMonographWithUnits(MonographDto monographDto, List<DigitalObjectDto<DomainObject>> children) {
        monographDto.setContainUnits(true);
        MonographWithUnits monographWithUnits = (MonographWithUnits) monographDto.toEntity();

        MonographUnit monographUnit;
        for (DigitalObjectDto<DomainObject> unitDto : children) {
            monographUnit = (MonographUnit) unitDto.accept(this);
            monographUnit.setParentId(monographWithUnits.getId());
            monographWithUnits.getMonographUnits().add(monographUnit);
        }

        return monographWithUnits;
    }

    private MonographWithPages getMonographWithPages(MonographDto monographDto, List<DigitalObjectDto<DomainObject>> children) {
        MonographWithPages monographWithPages = (MonographWithPages) monographDto.toEntity();

        Page page;
        for (DigitalObjectDto<DomainObject> pageDto : children) {
            page = (Page) pageDto.accept(this);
            page.setParentId(monographWithPages.getId());
            monographWithPages.getPages().add(page);
        }

        return monographWithPages;
    }

    @Override
    public MonographUnit assemble(MonographUnitDto monographUnitDto) {
        MonographUnit monographUnit = monographUnitDto.toEntity();

        List<DigitalObjectDto<Page>> children = getChildren(monographUnit.getId());

        Page page;
        for (DigitalObjectDto<Page> pageDto : children) {
            page = pageDto.accept(this);
            page.setParentId(monographUnit.getId());
            monographUnit.getPages().add(page);
        }

        return monographUnit;
    }

    @Override
    public Periodical assemble(PeriodicalDto periodicalDto) {
        Periodical periodical = periodicalDto.toEntity();

        List<DigitalObjectDto<PeriodicalVolume>> children = getChildren(periodical.getId());

        PeriodicalVolume periodicalVolume;
        for (DigitalObjectDto<PeriodicalVolume> periodicalVolumeDto : children) {
            periodicalVolume = periodicalVolumeDto.accept(this);
            periodicalVolume.setParentId(periodical.getId());
            periodical.getPeriodicalVolumes().add(periodicalVolume);
        }

        return periodical;
    }

    @Override
    public PeriodicalVolume assemble(PeriodicalVolumeDto periodicalVolumeDto) {
        PeriodicalVolume periodicalVolume = periodicalVolumeDto.toEntity();

        List<DigitalObjectDto<PeriodicalItem>> children = getChildren(periodicalVolume.getId());

        PeriodicalItem periodicalItem;
        for (DigitalObjectDto<PeriodicalItem> periodicalItemDto : children) {
            periodicalItem = periodicalItemDto.accept(this);
            periodicalItem.setParentId(periodicalVolume.getId());
            periodicalVolume.getPeriodicalItems().add(periodicalItem);
        }

        return periodicalVolume;
    }

    @Override
    public PeriodicalItem assemble(PeriodicalItemDto periodicalItemDto) {
        PeriodicalItem periodicalItem = periodicalItemDto.toEntity();

        List<DigitalObjectDto<Page>> children = getChildren(periodicalItem.getId());

        Page page;
        for (DigitalObjectDto<Page> pageDto : children) {
            page = pageDto.accept(this);
            page.setParentId(periodicalItem.getId());
            periodicalItem.getPages().add(page);
        }

        return periodicalItem;
    }

    @Override
    public Page assemble(PageDto page) {
        return page.toEntity();
    }

    private <T extends DomainObject> List<DigitalObjectDto<T>> getChildren(String parentId) {
        return dataProvider.getDigitalObjectsForParent(parentId);
    }
}

package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.KrameriusObject;
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
 * Assembles digital objects with its full hierarchy based on given DTO.
 *
 * @author Norbert Bodnar
 */
@Service
public class KrameriusPublicationAssembler implements KrameriusPublicationAssemblerVisitor {

    private final DataProvider dataProvider;

    @Autowired
    public KrameriusPublicationAssembler(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Monograph assemble(MonographDto monographDto) {
        List<DigitalObjectDto<KrameriusObject>> children = getChildren(monographDto.getPid());

        // children should never be empty or null;
        DigitalObjectDto<? extends KrameriusObject> firstChild = children.get(0);

        if (firstChild instanceof MonographUnitDto) {
            return getMonographWithUnits(monographDto, children);
        } else if (firstChild instanceof PageDto) {
            return getMonographWithPages(monographDto, children);
        } else {
            throw new IllegalStateException("Illegal type of children for monograph");
        }

    }

    private MonographWithUnits getMonographWithUnits(MonographDto monographDto, List<DigitalObjectDto<KrameriusObject>> children) {
        monographDto.setContainUnits(true);
        MonographWithUnits monographWithUnits = (MonographWithUnits) monographDto.toEntity();

        MonographUnit monographUnit;
        for (DigitalObjectDto<KrameriusObject> unitDto : children) {
            monographUnit = (MonographUnit) unitDto.accept(this);
            monographWithUnits.getMonographUnits().add(monographUnit);
        }

        return monographWithUnits;
    }

    private MonographWithPages getMonographWithPages(MonographDto monographDto, List<DigitalObjectDto<KrameriusObject>> children) {
        MonographWithPages monographWithPages = (MonographWithPages) monographDto.toEntity();

        Page page;
        for (DigitalObjectDto<KrameriusObject> pageDto : children) {
            page = (Page) pageDto.accept(this);
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
            periodicalItem.getPages().add(page);
        }

        return periodicalItem;
    }

    @Override
    public Page assemble(PageDto page) {
        return page.toEntity();
    }

    private <T extends KrameriusObject> List<DigitalObjectDto<T>> getChildren(String parentId) {
        return dataProvider.getDigitalObjectsForParent(parentId);
    }
}

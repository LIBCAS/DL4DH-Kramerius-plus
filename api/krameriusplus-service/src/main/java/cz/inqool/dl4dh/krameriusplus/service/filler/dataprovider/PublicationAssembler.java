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
        try {
            List<DigitalObjectDto<DomainObject>> children = dataProvider.getDigitalObjectsForParent(monographDto.getPid());

            if (children != null && !children.isEmpty()) {
                DigitalObjectDto<? extends DomainObject> child = children.get(0);

                if (child instanceof MonographUnitDto) {
                    monographDto.setContainUnits(true);
                    MonographWithUnits monographWithUnits = (MonographWithUnits) monographDto.toEntity();

                    for (DigitalObjectDto<DomainObject> dto : children) {
                        DomainObject monographUnit = dto.accept(this);
                        monographWithUnits.getMonographUnits().add((MonographUnit) monographUnit);
                    }

                    return monographWithUnits;
                } else if (child instanceof PageDto) {
                    MonographWithPages monographWithPages = (MonographWithPages) monographDto.toEntity();

                    for (var dto : children) {
                        var page = dto.accept(this);
                        monographWithPages.getPages().add((Page) page);
                    }

                    return monographWithPages;
                } else {
                    throw new IllegalStateException("Illegal type of children for monograph");
                }
            } else {
                throw new IllegalStateException("No children objects of monograph");
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException("Not implemented exception catch");
        }
    }

    @Override
    public MonographUnit assemble(MonographUnitDto monographUnitDto) {
        MonographUnit monographUnit = monographUnitDto.toEntity();

        List<DigitalObjectDto<Page>> children = getChildren(monographUnit.getPid());

        for (DigitalObjectDto<Page> pageDto : children) {
            monographUnit.getPages().add(pageDto.accept(this));
        }

        return monographUnit;
    }

    @Override
    public Periodical assemble(PeriodicalDto periodicalDto) {
        Periodical periodical = periodicalDto.toEntity();

        List<DigitalObjectDto<PeriodicalVolume>> children = getChildren(periodical.getPid());

        for (DigitalObjectDto<PeriodicalVolume> periodicalVolumeDto : children) {
            periodical.getPeriodicalVolumes().add(periodicalVolumeDto.accept(this));
        }

        return periodical;
    }

    @Override
    public PeriodicalVolume assemble(PeriodicalVolumeDto periodicalVolumeDto) {
        PeriodicalVolume periodicalVolume = periodicalVolumeDto.toEntity();

        List<DigitalObjectDto<PeriodicalItem>> children = getChildren(periodicalVolume.getPid());

        for (DigitalObjectDto<PeriodicalItem> periodicalItemDto : children) {
            periodicalVolume.getPeriodicalItems().add(periodicalItemDto.accept(this));
        }

        return periodicalVolume;
    }

    @Override
    public PeriodicalItem assemble(PeriodicalItemDto periodicalItemDto) {
        PeriodicalItem periodicalItem = periodicalItemDto.toEntity();

        List<DigitalObjectDto<Page>> children = getChildren(periodicalItem.getPid());

        for (DigitalObjectDto<Page> pageDto : children) {
            periodicalItem.getPages().add(pageDto.accept(this));
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

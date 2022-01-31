package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalItemDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalVolumeDto;

/**
 * @author Norbert Bodnar
 */
public interface KrameriusPublicationAssemblerVisitor {

    Monograph assemble(MonographDto monograph);

    MonographUnit assemble(MonographUnitDto monographUnit);

    Periodical assemble(PeriodicalDto periodical);

    PeriodicalVolume assemble(PeriodicalVolumeDto periodicalVolume);

    PeriodicalItem assemble(PeriodicalItemDto periodicalItem);

    Page assemble(PageDto page);
}

package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithUnits;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographWithPagesDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographWithUnitsDto;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class MonographProvider {

    private final PageProvider pageProvider;

    private final MonographUnitProvider monographUnitProvider;

    @Autowired
    public MonographProvider(PageProvider pageProvider, MonographUnitProvider monographUnitProvider) {
        this.pageProvider = pageProvider;
        this.monographUnitProvider = monographUnitProvider;
    }

    public MonographDto processMonograph(MonographDto monographDto) {
        log.info("Downloading pages for PID=" + monographDto.getPid() + ", " + monographDto.getTitle());
        SchedulerService.getTask(monographDto.getPid()).setState(EnrichmentTask.State.DOWNLOADING_PAGES);

        if (monographDto instanceof MonographWithPagesDto) {
            ((MonographWithPagesDto) monographDto).setPages(pageProvider.getDigitalObjectsForParent(monographDto.getPid()));
        } else if (monographDto instanceof MonographWithUnitsDto) {
            ((MonographWithUnitsDto) monographDto).setMonographUnits(monographUnitProvider.getMonographUnitsForParent(monographDto.getPid()));
        }

        return monographDto;
    }
}

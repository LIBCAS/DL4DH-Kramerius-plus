package cz.inqool.dl4dh.krameriusplus.service.filler.kramerius;

import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class KrameriusMonographProvider {

    private final KrameriusPageProvider pageProvider;

    private final KrameriusMonographUnitProvider monographUnitProvider;

    @Autowired
    public KrameriusMonographProvider(KrameriusPageProvider pageProvider, KrameriusMonographUnitProvider monographUnitProvider) {
        this.pageProvider = pageProvider;
        this.monographUnitProvider = monographUnitProvider;
    }

    public MonographDto processMonograph(MonographDto monographDto) {
        log.info("Downloading pages for PID=" + monographDto.getPid() + ", " + monographDto.getTitle());
        SchedulerService.getTask(monographDto.getPid()).setState(EnrichmentTask.State.DOWNLOADING_PAGES);

        try {
            monographDto.setPages(pageProvider.getPagesForParent(monographDto.getPid()));
        } catch (KrameriusException krameriusException) {
            // if could not map children to KrameriusPageDto class because of wrong model, do:
            monographDto.setMonographUnits(monographUnitProvider.getMonographUnitsForParent(monographDto.getPid()));
        }

        return monographDto;
    }
}

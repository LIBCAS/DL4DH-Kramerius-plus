package cz.inqool.dl4dh.krameriusplus.service.filler.kramerius;

import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.*;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class KrameriusMonographUnitProvider {

    private final String KRAMERIUS_ITEM_API;

    private final RestTemplate restTemplate;

    private final KrameriusPageProvider pageProvider;

    @Autowired
    public KrameriusMonographUnitProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi,
                                 RestTemplate restTemplate, KrameriusPageProvider pageProvider) {
        this.KRAMERIUS_ITEM_API = krameriusApi + "/search/api/v5.0/item/";
        this.restTemplate = restTemplate;
        this.pageProvider = pageProvider;
    }

    public MonographUnitDto processMonographUnit(MonographUnitDto monographUnitDto) {
        log.info("Downloading pages for PID=" + monographUnitDto.getPid() + ", " + monographUnitDto.getTitle());
        SchedulerService.getTask(monographUnitDto.getPid()).setState(EnrichmentTask.State.DOWNLOADING_PAGES);

        monographUnitDto.setPages(pageProvider.getPagesForParent(monographUnitDto.getPid()));

        return monographUnitDto;
    }

    public List<MonographUnitDto> getMonographUnitsForParent(String pid) {
        MonographUnitDto[] monographUnits;
        try {
            monographUnits = restTemplate.getForEntity(KRAMERIUS_ITEM_API + pid + "/children",
                    MonographUnitDto[].class).getBody();
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new KrameriusException(INVALID_MODEL, e);
            } else {
                throw new KrameriusException(EXTERNAL_API_ERROR, e);
            }
        }

        if (monographUnits == null || monographUnits.length == 0) {
            throw new KrameriusException(NO_CHILDREN, "Kramerius did not return any children for publication with PID=" + pid);
        }


        List<MonographUnitDto> result = new ArrayList<>();

        for (MonographUnitDto monographUnit : monographUnits) {
            if (monographUnit.getModel() == KrameriusModel.MONOGRAPH_UNIT) {
                monographUnit.setPages(pageProvider.getPagesForParent(monographUnit.getPid()));
                result.add(monographUnit);
            }
        }

        return result;
    }

}

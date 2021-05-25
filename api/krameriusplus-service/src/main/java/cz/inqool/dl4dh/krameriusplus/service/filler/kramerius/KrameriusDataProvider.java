package cz.inqool.dl4dh.krameriusplus.service.filler.kramerius;

import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.EXTERNAL_API_ERROR;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.INVALID_MODEL;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class KrameriusDataProvider {

    private final String KRAMERIUS_ITEM_API;

    private final RestTemplate restTemplate;

    private final KrameriusMonographProvider monographProvider;

    private final KrameriusMonographUnitProvider monographUnitProvider;

    @Autowired
    public KrameriusDataProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi,
                                 RestTemplate restTemplate, KrameriusMonographProvider monographProvider,
                                 KrameriusMonographUnitProvider monographUnitProvider) {
        this.KRAMERIUS_ITEM_API = krameriusApi + "/search/api/v5.0/item/";
        this.restTemplate = restTemplate;
        this.monographProvider = monographProvider;
        this.monographUnitProvider = monographUnitProvider;
    }

    public PublicationDto getPublication(String pid) {
        PublicationDto publicationDto = getRootDetails(pid);

        SchedulerService.getTask(pid).setTitle(publicationDto.getTitle());

        if (publicationDto instanceof MonographDto) {
            return monographProvider.processMonograph((MonographDto) publicationDto);
        } else if (publicationDto instanceof MonographUnitDto) {
            return monographUnitProvider.processMonographUnit((MonographUnitDto) publicationDto);
        } else if (publicationDto instanceof PeriodicalDto) {
            throw new UnsupportedOperationException("Periodicals not supported yet");
//            return processPeriodical((KrameriusPeriodicalDto) krameriusPublicationDto);
        }

        throw new IllegalStateException("No processing method for this type of publication");
    }

    private PublicationDto getRootDetails(String pid) {
        try {
            return restTemplate.getForObject(KRAMERIUS_ITEM_API + pid, PublicationDto.class);
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new KrameriusException(INVALID_MODEL, e);
            } else {
                throw new KrameriusException(EXTERNAL_API_ERROR, e);
            }
        }
    }

}

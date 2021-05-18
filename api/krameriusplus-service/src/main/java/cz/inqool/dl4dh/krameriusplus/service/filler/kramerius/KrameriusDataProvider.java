package cz.inqool.dl4dh.krameriusplus.service.filler.kramerius;

import cz.inqool.dl4dh.krameriusplus.dto.monograph.KrameriusMonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.KrameriusPeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPublicationDto;
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

    @Autowired
    public KrameriusDataProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi,
                                 RestTemplate restTemplate, KrameriusMonographProvider monographProvider) {
        this.KRAMERIUS_ITEM_API = krameriusApi + "/search/api/v5.0/item/";
        this.restTemplate = restTemplate;
        this.monographProvider = monographProvider;
    }

    public KrameriusPublicationDto getPublication(String pid) {
        KrameriusPublicationDto krameriusPublicationDto = getRootDetails(pid);

        SchedulerService.getTask(pid).setTitle(krameriusPublicationDto.getTitle());

        if (krameriusPublicationDto instanceof KrameriusMonographDto) {
            return monographProvider.processMonograph((KrameriusMonographDto) krameriusPublicationDto);
        } else if (krameriusPublicationDto instanceof KrameriusPeriodicalDto) {
            throw new UnsupportedOperationException("Periodicals not supported yet");
//            return processPeriodical((KrameriusPeriodicalDto) krameriusPublicationDto);
        }

        throw new IllegalStateException("No processing method for this type of publication");
    }

    private KrameriusPublicationDto getRootDetails(String pid) {
        try {
            return restTemplate.getForObject(KRAMERIUS_ITEM_API + pid, KrameriusPublicationDto.class);
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new KrameriusException(INVALID_MODEL, e);
            } else {
                throw new KrameriusException(EXTERNAL_API_ERROR, e);
            }
        }
    }

}

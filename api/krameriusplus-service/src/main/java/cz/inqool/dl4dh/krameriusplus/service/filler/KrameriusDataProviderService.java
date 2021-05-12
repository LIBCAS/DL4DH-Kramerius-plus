package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusMonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.domain.service.DataProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.NO_PAGES;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class KrameriusDataProviderService implements DataProviderService {

    private final String KRAMERIUS_ITEM_API = "https://kramerius.mzk.cz/search/api/v5.0/item/";

    private final RestTemplate restTemplate;

    @Autowired
    public KrameriusDataProviderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public KrameriusPublicationDto getPublication(String pid) {
        KrameriusPublicationDto krameriusPublicationDto = getRootPublication(pid);

        if (krameriusPublicationDto instanceof KrameriusMonographDto) {
            return processMonograph((KrameriusMonographDto) krameriusPublicationDto);
        } else if (krameriusPublicationDto instanceof KrameriusPeriodicalDto) {
            return processPeriodical((KrameriusPeriodicalDto) krameriusPublicationDto);
        }

        throw new IllegalStateException("No processing method for this type of publication");
    }

    private KrameriusPublicationDto getRootPublication(String pid) {
        return restTemplate.getForObject(KRAMERIUS_ITEM_API + pid, KrameriusPublicationDto.class);
    }

    private KrameriusMonographDto processMonograph(KrameriusMonographDto monographDto) {
        log.info("Downloading pages for PID=" + monographDto.getPid() + ", " + monographDto.getTitle());
        monographDto.setPages(getPagesForRootPid(monographDto.getPid()));

        return monographDto;
    }

    private KrameriusPeriodicalDto processPeriodical(KrameriusPeriodicalDto periodicalDto) {
        throw new UnsupportedOperationException("Cant parse periodicals yet");
    }

    /**
     * Gets a list of all child pages for given pid, and then adds text_ocr fields for every page
     * @param pid
     * @return
     */
    private List<KrameriusPageDto> getPagesForRootPid(String pid) {
        KrameriusPageDto[] pages = restTemplate.getForEntity(KRAMERIUS_ITEM_API + pid + "/children",
                KrameriusPageDto[].class).getBody();

        if (pages == null) {
            throw new KrameriusException(NO_PAGES, "Kramerius did not return any pages for publication with PID=" + pid);
        }

        List<KrameriusPageDto> result = new ArrayList<>();
        for (KrameriusPageDto page : pages) {
            appendTextOcr(page);
            result.add(page);
        }

        return result;
    }

    private void appendTextOcr(KrameriusPageDto page) {
        String pageContent = restTemplate.getForObject(KRAMERIUS_ITEM_API + page.getPid() + "/streams/TEXT_OCR",
                String.class);

        // streams/TEXT_OCR kramerius api returns BOM(\uFEFF) as first character, which is a character counted
        // in offsets of the produced tokens in UDPipe

        if (pageContent != null && !pageContent.isEmpty()) {
            page.setTextOcr(pageContent.replace("\uFEFF", ""));
        }
    }
}

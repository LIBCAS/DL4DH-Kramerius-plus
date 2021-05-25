package cz.inqool.dl4dh.krameriusplus.service.filler.kramerius;

import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
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
@Slf4j
@Service
public class KrameriusPageProvider {

    private final String KRAMERIUS_ITEM_API;

    private final RestTemplate restTemplate;

    @Autowired
    public KrameriusPageProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi,
                                 RestTemplate restTemplate) {
        this.KRAMERIUS_ITEM_API = krameriusApi + "/search/api/v5.0/item/";
        this.restTemplate = restTemplate;
    }

    /**
     * Gets a list of all child pages for given pid, and then adds text_ocr fields for every page
     *
     * @param parentId id of the parent publication, must contain pages as children
     * @return list of Page DTOs for given parent
     */
    public List<KrameriusPageDto> getPagesForParent(String parentId) {
        KrameriusPageDto[] pages;
        try {
            pages = restTemplate.getForEntity(KRAMERIUS_ITEM_API + parentId + "/children",
                    KrameriusPageDto[].class).getBody();
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new KrameriusException(INVALID_MODEL, e);
            } else {
                throw new KrameriusException(EXTERNAL_API_ERROR, e);
            }
        }

        if (pages == null) {
            throw new KrameriusException(NO_CHILDREN, "Kramerius did not return any children for publication with PID=" + parentId);
        }

        List<KrameriusPageDto> result = new ArrayList<>();

        for (KrameriusPageDto page : pages) {
            if (page.getModel() == KrameriusModel.PAGE) {
                page.setParentId(parentId);
                page.setTextOcr(getTextOcr(page.getPid()));
                result.add(page);
            }
        }

        return result;
    }

    private String getTextOcr(String id) {
        try {
            String pageContent = restTemplate.getForObject(KRAMERIUS_ITEM_API + id + "/streams/TEXT_OCR",
                    String.class);

            // streams/TEXT_OCR kramerius api returns BOM(\uFEFF) as first character, which is a character counted
            // in offsets of the produced tokens in UDPipe

            if (pageContent != null && !pageContent.isEmpty()) {
                pageContent = pageContent
                        .replace("\uFEFF", "")
                        .replaceAll("-\r\n", "")
                        .replaceAll("\r\n", " ")
                        .replaceAll("-\n", "")
                        .replaceAll("\n", " ");
            }

            return pageContent;
        } catch (RestClientException exception) {
            log.error("Error downloading content for page with PID=" + id, exception);
            return "";
        }
    }
}

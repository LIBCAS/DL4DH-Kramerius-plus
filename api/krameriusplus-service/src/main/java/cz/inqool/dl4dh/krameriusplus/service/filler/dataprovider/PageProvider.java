package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
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
import java.util.Collection;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.*;

/**
 * @author Norbert Bodnar
 */
@Slf4j
@Service
public class PageProvider implements DataProvider<PageDto>{

    private final String KRAMERIUS_ITEM_API;

    private final RestTemplate restTemplate;

    @Autowired
    public PageProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi,
                        RestTemplate restTemplate) {
        this.KRAMERIUS_ITEM_API = krameriusApi + "/search/api/v5.0/item/";
        this.restTemplate = restTemplate;
    }

    @Override
    public PageDto getDigitalObject(String objectId) {
        PageDto page = restTemplate.getForEntity(KRAMERIUS_ITEM_API + objectId,
                PageDto.class).getBody();

        notNull(page, () -> new KrameriusException(MISSING_OBJECT, "No digital object of model Page for ID=" + objectId));

        page.setTextOcr(getTextOcr(objectId));

        return page;
    }


    /**
     * Gets a list of all child pages for given pid, and then adds text_ocr fields for every page
     *
     * @param parentId id of the parent publication, must contain pages as children
     * @return list of Page DTOs for given parent
     */
    @Override
    public List<PageDto> getDigitalObjectsForParent(String parentId) {
        PageDto[] pages;
        try {
            pages = restTemplate.getForEntity(KRAMERIUS_ITEM_API + parentId + "/children",
                    PageDto[].class).getBody();
        } catch (RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new KrameriusException(INVALID_MODEL, e);
            } else {
                throw new KrameriusException(EXTERNAL_API_ERROR, e);
            }
        }

        if (pages == null) {
            throw new KrameriusException(MISSING_CHILDREN, "Kramerius did not return any children for publication with PID=" + parentId);
        }

        List<PageDto> result = new ArrayList<>();

        for (PageDto page : pages) {
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

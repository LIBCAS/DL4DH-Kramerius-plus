package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.function.Supplier;

import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.EXTERNAL_API_ERROR;
import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.NOT_RESPONDING;
import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.UNAUTHORIZED;
import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.UNDEFINED;

public class KrameriusUtils {

    public static <T> T tryKrameriusCall(Supplier<T> call) {
        try {
            return call.get();
        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 404) {
                throw new KrameriusException(NOT_FOUND, e);
            }
            if (e.getRawStatusCode() == 403) {
                throw new KrameriusException(UNAUTHORIZED, e);
            }
            throw new KrameriusException(EXTERNAL_API_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new KrameriusException(NOT_RESPONDING, e);
        } catch (Exception e) {
            throw new KrameriusException(UNDEFINED, e);
        }
    }

    public static String buildUriPath(String... segments) {
        return UriComponentsBuilder.newInstance()
                .pathSegment(segments)
                .build()
                .toUriString();
    }

    public static String normalizeText(String textOcr) {
        return textOcr.replace("\uFEFF", "")
                .replaceAll("\\S-\r\n", "")
                .replaceAll("\\S-\n", "")
                .replaceAll("\\S–\r\n", "")
                .replaceAll("\\S–\n", "")
                .replaceAll("\r\n", " ")
                .replaceAll("\n", " ");
    }

    public static void setParentId(String parentId, List<DigitalObject> result) {
        int index = 0;

        if (result != null) {
            // publications and pages will share indices
            for (DigitalObject child : result) {
                child.setParentId(parentId);
                child.setIndex(index++);
            }
        }
    }
}

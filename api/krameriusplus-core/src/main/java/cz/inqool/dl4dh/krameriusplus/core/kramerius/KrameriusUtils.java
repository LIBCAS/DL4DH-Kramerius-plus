package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Supplier;

import static cz.inqool.dl4dh.krameriusplus.api.exception.KrameriusException.ErrorCode.*;

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
}

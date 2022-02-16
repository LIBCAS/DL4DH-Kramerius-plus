package cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

public class HttpHeadersBuilder {

    private final HttpHeaders httpHeaders = new HttpHeaders();


    public HttpHeadersBuilder contentType(@NonNull MediaType mediaType) {
        httpHeaders.setContentType(mediaType);
        return this;
    }

    public HttpHeaders build() {
        return httpHeaders;
    }
}

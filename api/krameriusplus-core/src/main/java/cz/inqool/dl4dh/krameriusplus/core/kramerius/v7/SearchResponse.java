package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectCreateDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SearchResponse {

    private Response response;

    private ResponseHeader responseHeader;

    @Getter
    @Setter
    public static class Response{

        private List<DigitalObjectCreateDto> docs;

        private Integer numFound;

        private boolean numFoundExact;
    }

    @Getter
    @Setter
    public static class ResponseHeader {

        private Integer status;
    }
}

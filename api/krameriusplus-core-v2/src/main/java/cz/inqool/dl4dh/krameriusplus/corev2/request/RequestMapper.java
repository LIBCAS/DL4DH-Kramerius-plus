package cz.inqool.dl4dh.krameriusplus.corev2.request;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.RequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.mapper.DatedObjectMapper;

import java.util.*;

public interface RequestMapper<ENTITY extends Request, CDTO extends RequestCreateDto, DTO extends RequestDto>
        extends DatedObjectMapper<ENTITY, CDTO, DTO> {

    default Map<Long, String> publicationIdsFromDto(List<String> publicationIds) {
        Map<Long, String> result = new HashMap<>();
        long order = 0;

        for (String publicationId : publicationIds) {
            result.put(order++, publicationId);
        }

        return result;
    }

    default List<String> publicationIdsToDto(Map<Long, String> publicationIds) {
        return new ArrayList<>(new TreeMap<>(publicationIds).values());
    }
}

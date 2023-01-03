package cz.inqool.dl4dh.krameriusplus.core.request;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.RequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.user.UserProvider;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class RequestMapper<ENTITY extends Request, CDTO extends RequestCreateDto, DTO extends RequestDto>
        implements DatedObjectMapper<ENTITY, CDTO, DTO> {

    @Autowired
    protected UserProvider userProvider;

    public Map<Long, String> publicationIdsFromDto(List<String> publicationIds) {
        Map<Long, String> result = new HashMap<>();
        long order = 0;

        for (String publicationId : publicationIds) {
            result.put(order++, publicationId);
        }

        return result;
    }

    public List<String> publicationIdsToDto(Map<Long, String> publicationIds) {
        return new ArrayList<>(new TreeMap<>(publicationIds).values());
    }

    @Mapping(target = "owner", expression = "java(userProvider.getCurrentUser())")
    public abstract ENTITY fromCreateDto(CDTO dto);
}

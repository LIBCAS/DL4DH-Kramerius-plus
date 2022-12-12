package cz.inqool.dl4dh.krameriusplus.corev2.request;

import cz.inqool.dl4dh.krameriusplus.api.RequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.RequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.user.User;
import lombok.Getter;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

@RequestScope
public abstract class RequestMapper<ENTITY extends Request, CDTO extends RequestCreateDto, DTO extends RequestDto>
        implements DatedObjectMapper<ENTITY, CDTO, DTO> {

    @Autowired
    @Getter
    protected User user;

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

    @Mapping(target = "owner", expression = "java(user)")
    public abstract ENTITY fromCreateDto(CDTO dto);
}

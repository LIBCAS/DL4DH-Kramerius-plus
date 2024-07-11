package cz.inqool.dl4dh.krameriusplus.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListFilterDto {

    @Builder.Default
    RootFilterOperation rootFilterOperation = RootFilterOperation.AND;

    Integer year;

    Integer identification;

    UserRequestState state;

    UserRequestType type;

    String username;

    @Builder.Default
    Sort.Order order = Sort.Order.DESC;

    @Builder.Default
    Sort.Field field = Sort.Field.CREATED;

    public enum  RootFilterOperation {
        AND,
        OR
    }
}

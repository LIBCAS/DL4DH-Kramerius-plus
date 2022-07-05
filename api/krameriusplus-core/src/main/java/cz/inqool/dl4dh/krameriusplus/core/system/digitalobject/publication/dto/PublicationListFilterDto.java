package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PublicationListFilterDto {

    private String title;

    private String parentId;

    private Instant createdBefore;

    private Instant createdAfter;

    private Boolean isPublished;

    private Instant publishedBefore;

    private Instant publishedAfter;
}

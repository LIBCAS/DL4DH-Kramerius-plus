package cz.inqool.dl4dh.krameriusplus.api.publication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationFilter {

    private String uuid;

    private String title;

    private String parentId;

    private String model;

    private Boolean isRootEnrichment;

    private Instant createdBefore;

    private Instant createdAfter;

    private Boolean isPublished;

    private Instant publishedBefore;

    private Instant publishedAfter;
}
